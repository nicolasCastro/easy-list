package com.thinkup.sampleeasy

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.viewbinding.ViewBinding

class CustomStringView(context: Context, attrs: AttributeSet?) : TextView(context, attrs), ViewBinding {
    init {
        textSize = 20.0f
    }

    fun bind(text: String) {
        this.text = text
    }

    override fun getRoot(): View {
        return this
    }
}