package com.thinkup.easypagedlist.core

import com.thinkup.easycore.RendererItem


interface IDataSource<T> {
    fun getKeys(items: List<*>)
    suspend fun getInitial(): List<*>
    suspend fun getReinitial(): List<*>
    suspend fun getAfter(): List<*>
    suspend fun getBefore(): List<*>
    fun create(inputSource: MutableList<RendererItem<*>> = mutableListOf(), firstInstance: Boolean = true): T
}