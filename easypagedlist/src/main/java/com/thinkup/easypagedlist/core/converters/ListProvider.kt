package com.thinkup.easypagedlist.core.converters

class ListProvider<T>(val list: List<T>) {
    fun getList(page: Int, pageSize: Int): List<T> {
        val initialIndex = page * pageSize
        val finalIndex = initialIndex + pageSize
        return list.subList(initialIndex, finalIndex)
    }
}