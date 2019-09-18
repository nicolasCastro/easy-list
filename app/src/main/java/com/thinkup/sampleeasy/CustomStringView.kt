package com.thinkup.sampleeasy

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class CustomStringView(context: Context, attrs: AttributeSet?) : TextView(context, attrs) {
    init {
        textSize = 20.0f
    }

    fun bind(text: String) {
        this.text = text
    }
}