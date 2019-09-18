package com.thinkup.easypagedlist.core

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.thinkup.easycore.RendererItem

class RendererDataSourceFactory<T : RendererDataSource<T>>(private val dataSource: T) : DataSource.Factory<Int, RendererItem<*>>() {

    private var overInstance: Boolean = false
    private var currentInstance: T = dataSource
    val dataSourceLiveData = MutableLiveData<T>()

    fun remove(position: Int) {
        currentInstance.items.removeAt(position)
        overInstance = true
        invalidate()
    }

    fun invalidate() = dataSourceLiveData.value?.invalidate()

    override fun create(): DataSource<Int, RendererItem<*>> {
        if (!overInstance)
            currentInstance = dataSource.create(firstInstance = dataSourceLiveData.value == null)
        else
            currentInstance = dataSource.create(currentInstance.items, dataSourceLiveData.value == null)
        dataSourceLiveData.postValue(currentInstance)

        overInstance = false
        return currentInstance
    }
}