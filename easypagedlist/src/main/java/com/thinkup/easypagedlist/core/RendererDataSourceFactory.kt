package com.thinkup.easypagedlist.core

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.thinkup.easycore.RendererItem

class RendererDataSourceFactory<T : RendererDataSource<T>>(private val dataSource: T) : DataSource.Factory<Int, RendererItem<*>>() {

    private var overInstance: Boolean = false
    private var currentInstance: T = dataSource
    val dataSourceLiveData = MutableLiveData<T>()
    private var filterManager: FilterManager<*>? = null

    fun remove(position: Int) {
        currentInstance.items.removeAt(position)
        overInstance = true
        invalidate()
    }

    fun <R> updateFilter(filterManager: FilterManager<R>? = null) {
        invalidate()
        this.filterManager = filterManager
    }

    fun invalidate() = dataSourceLiveData.value?.invalidate()

    override fun create(): DataSource<Int, RendererItem<*>> {
        currentInstance = if (!overInstance)
            dataSource.create(firstInstance = dataSourceLiveData.value == null)
        else
            dataSource.create(currentInstance.items, dataSourceLiveData.value == null)
        filterManager?.let {
            currentInstance.updateFilter(filterManager)
            filterManager = null
        }
        dataSourceLiveData.postValue(currentInstance)

        overInstance = false
        return currentInstance
    }
}