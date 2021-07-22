package com.thinkup.easypagedlist.core.converters

import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList

class DataSource<T: Any>(private val provider: ListProvider<T>) : PageKeyedDataSource<Int, T>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        val list = provider.getList(0, params.requestedLoadSize)
        callback.onResult(list, 1, 2)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val list = provider.getList(params.key, params.requestedLoadSize)
        callback.onResult(list, params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val list = provider.getList(params.key, params.requestedLoadSize)
        val nextIndex = if (params.key > 1) params.key - 1 else null
        callback.onResult(list, nextIndex)
    }

    companion object {
        fun default(size: Int) = PagedList.Config.Builder()
            .setInitialLoadSizeHint(size)
            .setPageSize(if (size == 0) 1 else size)
            .build()
    }
}