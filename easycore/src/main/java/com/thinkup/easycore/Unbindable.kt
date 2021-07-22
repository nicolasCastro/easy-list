package com.thinkup.easycore

interface Unbindable<VB, M> {
    fun unbind(binding: VB, model: M, position: Int)
}