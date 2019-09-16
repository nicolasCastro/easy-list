package com.thinkup.easypagedlist.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

abstract class ViewRenderer<M : Any, VT : View> {
    private var type: String

    constructor(type: KClass<M>) {
        this.type =
            type.qualifiedName ?: throw RuntimeException("Couldn't determine qualifiedName for supplied KClass.")
    }

    constructor(type: String) {
        this.type = type
    }

    constructor(type: Int) {
        this.type = type.toString()
    }

    constructor(type: Enum<*>) {
        this.type = type.toString()
    }

    fun getType(): String = type

    abstract fun create(parent: ViewGroup): VT

    abstract fun bind(view: VT, model: M, position: Int)

    open fun animateIn(view: VT, startDelay: Long): Long? = null

    protected fun inflate(@LayoutRes layout: Int, parent: ViewGroup, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(parent.context).inflate(layout, parent, attachToRoot)
    }
}