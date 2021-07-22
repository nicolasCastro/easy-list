package com.thinkup.sampleeasy

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thinkup.easycore.ViewRenderer
import com.thinkup.sampleeasy.databinding.ItemSampleBinding

class SampleRenderer : ViewRenderer<SampleItem, ItemSampleBinding>(SampleItem::class) {

    override val create: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> ItemSampleBinding
        get() = ItemSampleBinding::inflate

    override fun bind(binding: ItemSampleBinding, model: SampleItem, position: Int) {
        with(binding) {
            sampleName.text = model.name
            sampleGender.text = model.gender
            sampleCompany.text = model.company
        }
    }
}