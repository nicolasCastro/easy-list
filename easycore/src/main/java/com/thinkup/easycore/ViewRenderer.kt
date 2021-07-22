package com.thinkup.easycore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

abstract class ViewRenderer<M : Any, VB : ViewBinding> : Unbindable<VB, M> {
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

    abstract val create: (layoutIflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> VB

    abstract fun bind(binding: VB, model: M, position: Int)

    override fun unbind(binding: VB, model: M, position: Int) {}

    open fun animateIn(binding: VB, startDelay: Long): Long? = null
}