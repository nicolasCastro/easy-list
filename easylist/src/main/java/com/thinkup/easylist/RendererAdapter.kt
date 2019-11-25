package com.thinkup.easylist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thinkup.easycore.RendererItem
import com.thinkup.easycore.RendererViewHolder
import com.thinkup.easycore.RendererViewModel
import com.thinkup.easycore.ViewRenderer

class RendererAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: MutableList<RendererItem<*>> = mutableListOf()
    private val types: LinkedHashSet<String> = linkedSetOf()
    private val renderers: ArrayList<ViewRenderer<Any, View>> = arrayListOf()

    fun setItems(items: List<Any>) {
        this.items.clear()
        this.items.addAll(wrapItems(items))
        notifyDataSetChanged()
    }

    fun setItemsWithoutNotify(items: List<Any>) {
        this.items.clear()
        this.items.addAll(wrapItems(items))
    }

    fun getItems() = unwrap()

    fun addItem(item: Any, position: Int = -1) {
        val addPosition = if (position == -1) items.size else position
        items.add(position, wrapItem(item))
        notifyItemInserted(position)
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        RendererViewHolder(renderers[viewType].create(parent))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val renderer = getRenderer(position)
        val item = getItem(position)
        renderer.bind(holder.itemView, item.viewModel, position)
    }

    override fun getItemViewType(position: Int) = getTypeIndex(position)

    fun addRenderer(renderer: ViewRenderer<out Any, out View>) {
        if (types.contains(renderer.getType())) {
            return //throw RuntimeException("A ViewRenderer for item type '${renderer.getType()}' has already been added.")
        } else {
            types.add(renderer.getType())
            renderers.add(renderer as ViewRenderer<Any, View>)
        }
    }

    private fun wrapItems(items: List<Any>): List<RendererItem<Any>> = items.map { wrapItem(it) }

    private fun <T> wrapItem(item: T): RendererItem<T> = RendererItem(item)

    private fun unwrap(): List<Any> =
        items.map { any -> any.viewModel.let { it } ?: run { /*next*/ } }

    private fun getRenderer(position: Int): ViewRenderer<Any, View> {
        val kclassType = getKClassType(position)
        val stringType = getStringType(position)
        val index = getTypeIndex(position, kclassType, stringType)
        return renderers.getOrNull(index)
            ?: throw RuntimeException("No ViewRenderer registered for item type: $kclassType/$stringType")
    }

    private fun getTypeIndex(
        position: Int = -1,
        kclassType: String? = null,
        stringType: String? = null
    ): Int {
        val kclass = kclassType ?: getKClassType(position)
        val string = stringType ?: getStringType(position)
        return when {
            types.contains(kclass) -> types.indexOf(kclass)
            types.contains(string) -> types.indexOf(string)
            else -> throw RuntimeException("No ViewRenderer registered for item type: $kclass/$string")
        }
    }

    private fun getItem(position: Int): RendererItem<Any> = items[position] as RendererItem<Any>

    fun get(position: Int): Any = (items[position] as RendererItem<Any>).viewModel

    private fun getViewModel(position: Int): Any = items[position].viewModel!!

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
}