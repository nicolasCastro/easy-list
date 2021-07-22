package com.thinkup.easypagedlist.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.thinkup.easycore.RendererItem
import com.thinkup.easycore.RendererViewHolder
import com.thinkup.easycore.RendererViewModel
import com.thinkup.easycore.ViewRenderer
import com.thinkup.easypagedlist.databinding.FooterErrorBinding
import com.thinkup.easypagedlist.databinding.FooterLoadingBinding

class RendererPagedAdapter(
    private val errorCallback: RetryCallback? = null,
    private var footerLoadingBinding: ViewBinding? = null,
    private var footerErrorBinding: ViewBinding? = null
) : PagedListAdapter<RendererItem<*>, RecyclerView.ViewHolder>(adapterCallback) {
    private val types: LinkedHashSet<String> = linkedSetOf()
    private val renderers: ArrayList<ViewRenderer<Any, ViewBinding>> = arrayListOf()
    private var state = RendererDataSource.State.LOADING

    fun setFooterBinding(footerViewBinding: ViewBinding? = null, errorViewBinding: ViewBinding? = null) {
        footerLoadingBinding = footerViewBinding
        footerErrorBinding = errorViewBinding
    }

    private fun getFooterBinding(): ViewBinding? =
        if (state == RendererDataSource.State.ERROR) footerErrorBinding
        else footerLoadingBinding

    private fun getDefaultFooter(parent: ViewGroup): ViewBinding? =
        if (state == RendererDataSource.State.ERROR)
            FooterErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        else FooterLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER_VIEW_TYPE) {
            val footerBinding = if (getFooterBinding() == null) getDefaultFooter(parent)
            else footerLoadingBinding
            Footer(requireNotNull(footerBinding))
        } else {
            RendererViewHolder(renderers[viewType].create(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == FOOTER_VIEW_TYPE) {
            (holder as Footer).bind(state, errorCallback)
        } else {
            val bindingHolder = holder as RendererViewHolder
            val renderer = getRenderer(position)
            val item = getWrapItem(position)
            renderer.bind(bindingHolder.binding, item.viewModel!!, position)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.absoluteAdapterPosition
        if (position != -1 && position < itemCount && getItemViewType(position) != FOOTER_VIEW_TYPE) {
            val bindingHolder = holder as RendererViewHolder
            val renderer = getRenderer(position)
            val item = getWrapItem(position)
            renderer.unbind(bindingHolder.binding, item.viewModel!!, position)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) getTypeIndex(position)
        else FOOTER_VIEW_TYPE
    }

    fun addRenderer(renderer: ViewRenderer<out Any, out ViewBinding>) {
        if (types.contains(renderer.getType())) {
            return //throw RuntimeException("A ViewRenderer for item type '${renderer.getType()}' has already been added.")
        } else {
            types.add(renderer.getType())
            renderers.add(renderer as ViewRenderer<Any, ViewBinding>)
        }
    }

    private fun getRenderer(position: Int): ViewRenderer<Any, ViewBinding> {
        val kclassType = getKClassType(position)
        val stringType = getStringType(position)
        val index = getTypeIndex(position, kclassType, stringType)
        return renderers.getOrNull(index)
            ?: throw RuntimeException("No ViewRenderer registered for item type: $kclassType/$stringType")
    }

    private fun getTypeIndex(position: Int = -1, kclassType: String? = null, stringType: String? = null): Int {
        val kclass = kclassType ?: getKClassType(position)
        val string = stringType ?: getStringType(position)
        return when {
            types.contains(kclass) -> types.indexOf(kclass)
            types.contains(string) -> types.indexOf(string)
            else -> throw RuntimeException("No ViewRenderer registered for item type: $kclass/$string")
        }
    }

    fun getWrapItem(position: Int): RendererItem<*> = getItem(position)!!

    fun get(position: Int): Any = (getWrapItem(position) as RendererItem<Any>).viewModel

    private fun getViewModel(position: Int): Any = getWrapItem(position).viewModel!!

    private fun getKClassType(position: Int): String = getViewModel(position)::class.qualifiedName
        ?: throw RuntimeException("Could not obtain qualifiedName from ViewModel KClass.")

    private fun getStringType(position: Int): String? {
        getViewModel(position).apply {
            if (this is RendererViewModel) {
                return getType()
            }
        }
        return null
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == RendererDataSource.State.LOADING || state == RendererDataSource.State.ERROR)
    }

    fun setState(state: RendererDataSource.State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    class Footer(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(state: RendererDataSource.State, errorCallback: RetryCallback?) {
            itemView.setOnClickListener {
                if (state == RendererDataSource.State.ERROR) errorCallback?.onError()
            }
        }
    }

    interface RetryCallback {
        fun onError()
    }

    companion object {
        const val FOOTER_VIEW_TYPE = -1
        private val adapterCallback = object : DiffUtil.ItemCallback<RendererItem<*>>() {

            override fun areItemsTheSame(old: RendererItem<*>, new: RendererItem<*>): Boolean {
                return old.viewModel?.hashCode() == new.viewModel?.hashCode()
            }

            override fun areContentsTheSame(oldTrip: RendererItem<*>, newTrip: RendererItem<*>): Boolean {
                return oldTrip.viewModel.toString() == newTrip.viewModel.toString()
            }
        }
    }
}