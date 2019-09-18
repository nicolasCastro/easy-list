package com.thinkup.sampleeasy

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.thinkup.easycore.ViewRenderer
import kotlinx.android.synthetic.main.item_sample.view.*

class ColoredRenderer : ViewRenderer<ColoredItem, View>(ColoredItem::class) {
    override fun create(parent: ViewGroup): View = inflate(R.layout.item_sample, parent, false)

    override fun bind(view: View, model: ColoredItem, position: Int) {
        view.setBackgroundColor(model.color)
    }
}