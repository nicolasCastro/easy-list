package com.thinkup.easycore

interface Unbindable<VT, M> {
    fun unbind(view: VT, model: M, position: Int)
}