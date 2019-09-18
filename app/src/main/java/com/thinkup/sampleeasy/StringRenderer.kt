package com.thinkup.sampleeasy

import android.view.ViewGroup
import com.thinkup.easycore.ViewRenderer

class StringRenderer : ViewRenderer<String, CustomStringView>(String::class) {
    override fun create(parent: ViewGroup): CustomStringView = CustomStringView(parent.context, null)

    override fun bind(view: CustomStringView, model: String, position: Int) {
        view.bind(model)
    }
}