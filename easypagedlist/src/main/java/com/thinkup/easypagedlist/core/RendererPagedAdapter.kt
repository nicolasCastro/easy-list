package com.thinkup.easypagedlist.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thinkup.easypagedlist.R
import com.thinkup.easycore.RendererItem
import com.thinkup.easycore.RendererViewHolder
import com.thinkup.easycore.RendererViewModel
import com.thinkup.easycore.ViewRenderer

class RendererPagedAdapter(
    private val errorCallback: RetryCallback? = null,
    @LayoutRes private var footerLoadingLayout: Int = R.layout.footer_loading,
    @LayoutRes private var footerErrorLayout: Int = R.layout.footer_error
) : PagedListAdapter<RendererItem<*>, RecyclerView.ViewHolder>(adapterCallback) {
    private val types: LinkedHashSet<String> = linkedSetOf()
    private val renderers: ArrayList<ViewRenderer<Any, View>> = arrayListOf()
    private var state = RendererDataSource.State.LOADING

    private fun getFooterLayout(): Int =
        if (state == RendererDataSource.State.ERROR) footerErrorLayout
        else footerLoadingLayout

    fun setFooterLayout(@LayoutRes loading: Int = R.layout.footer_loading, @LayoutRes error: Int = R.layout.footer_error) {
        footerLoadingLayout = loading
        footerErrorLayout = error
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER_VIEW_TYPE) {
            Footer(LayoutInflater.from(parent.context).inflate(getFooterLayout(), parent, false))
        } else {
            RendererViewHolder(renderers[viewType].create(parent))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == FOOTER_VIEW_TYPE) {
            (holder as Footer).bind(state, errorCallback)
        } else {
            val renderer = getRenderer(position)
            val item = getWrapItem(position)
            renderer.bind(holder.itemView, item.viewModel!!, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) getTypeIndex(position)
        else FOOTER_VIEW_TYPE
    }

    fun addRenderer(renderer: ViewRenderer<out Any, out View>) {
        if (types.contains(renderer.getType())) {
            return //throw RuntimeException("A ViewRenderer for item type '${renderer.getType()}' has already been added.")
        } else {
            types.add(renderer.getType())
            renderers.add(renderer as ViewRenderer<Any, View>)
        }
    }

    private fun getRenderer(position: Int): ViewRenderer<Any, View> {
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

    class Footer(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        private const val FOOTER_VIEW_TYPE = -1
        private val adapterCallback = object : DiffUtil.ItemCallback<RendererItem<*>>() {

            override fun areItemsTheSame(oldTrip: RendererItem<*>, newTrip: RendererItem<*>): Boolean {
                return oldTrip.viewModel.hashCode() == newTrip.viewModel.hashCode()
            }

            override fun areContentsTheSame(oldTrip: RendererItem<*>, newTrip: RendererItem<*>): Boolean {
                return oldTrip.viewModel.toString() == newTrip.viewModel.toString()
            }
        }
    }
}