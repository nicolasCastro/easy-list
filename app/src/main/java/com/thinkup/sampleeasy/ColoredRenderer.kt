package com.thinkup.sampleeasy

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thinkup.easycore.ViewRenderer
import com.thinkup.sampleeasy.databinding.ItemSampleBinding

class ColoredRenderer(
    private val onRemoveClick: ((Any) -> Unit)?,
) : ViewRenderer<ColoredItem, ItemSampleBinding>(ColoredItem::class) {

    override val create: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> ItemSampleBinding
        get() = ItemSampleBinding::inflate

    override fun bind(binding: ItemSampleBinding, model: ColoredItem, position: Int) {
        with(binding.root) {
            setBackgroundColor(model.color)
            setOnClickListener {
                onRemoveClick?.invoke(model)
            }
        }
    }

}