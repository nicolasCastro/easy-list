package com.thinkup.sampleeasy

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thinkup.easycore.ViewRenderer

class StringRenderer : ViewRenderer<String, CustomStringView>(String::class) {

    override val create: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> CustomStringView
        get() = { _, parent, _ -> CustomStringView(parent.context, null) }

    override fun bind(binding: CustomStringView, model: String, position: Int) {
        binding.bind(model)
    }
}