package com.thinkup.easypagedlist.core

interface FilterManager<T> {
    fun clear()
    fun getFilter(): T
    fun setFilter(new: T)
}