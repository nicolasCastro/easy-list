package com.thinkup.easypagedlist

import android.view.View
import android.view.ViewGroup
import com.thinkup.easypagedlist.core.adapter.ViewRenderer
import kotlinx.android.synthetic.main.item_sample.view.*

class SampleRenderer : ViewRenderer<SampleItem, View>(SampleItem::class) {
    override fun create(parent: ViewGroup): View = inflate(R.layout.item_sample, parent, false)

    override fun bind(view: View, model: SampleItem, position: Int) {
        view.sampleName.text = model.name
        view.sampleGender.text = model.gender
        view.sampleCompany.text = model.company
    }
}