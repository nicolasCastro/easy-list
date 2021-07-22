package com.thinkup.easypagedlist.core

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.thinkup.easycore.RendererItem
import com.thinkup.easypagedlist.core.converters.DataSource
import com.thinkup.easypagedlist.core.converters.ListProvider
import kotlinx.coroutines.*
import java.util.concurrent.Executors

abstract class RendererDataSource<T>(
    val items: MutableList<RendererItem<*>> = mutableListOf(),
    private val firstInstance: Boolean = true
) : PageKeyedDataSource<Int, RendererItem<*>>(), IDataSource<T> {

    val state = MutableLiveData<State>()
    private var retryBlock: (() -> Unit)? = null
    protected var previousKey: Any? = null
    protected var nextKey: Any? = null
    protected var filterManager: FilterManager<*>? = null

    private fun inputSource(): List<*> {
        val unwrapped = items.map { it.viewModel }
        items.clear()
        return unwrapped
    }

    fun <R> updateFilter(filterManager: FilterManager<R>?) {
        this.filterManager = filterManager
    }

    override suspend fun getReinitial(): List<*> = getInitial()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, RendererItem<*>>) {
        executeService(State.INITIAL) {
            val initial = when {
                items.isNotEmpty() -> inputSource()
                firstInstance -> getInitial()
                else -> getReinitial()
            }
            getKeys(initial)
            val wrapped = convert(wrapItems(initial))
            callback.onResult(wrapped, null, 2)
            setRetry { loadInitial(params, callback) }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RendererItem<*>>) {
        executeService {
            if (state.value != State.FINISHED) {
                val page = getAfter()
                if (page.isNotEmpty()) {
                    getKeys(page)
                    val wrapped = convert(wrapItems(page))
                    callback.onResult(wrapped, params.key + 1)
                } else {
                    state.postValue(State.FINISHED)
                }
            }
            setRetry { loadAfter(params, callback) }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RendererItem<*>>) {
        executeService {
            val page = getBefore()
            getKeys(page)
            val wrapped = convert(wrapItems(page))
            callback.onResult(wrapped, params.key - 1)
            setRetry { loadBefore(params, callback) }
        }
    }

    private fun wrapItems(items: List<*>): List<RendererItem<*>> {
        val wrapped = items.map { wrapItem(it) }
        this.items.addAll(wrapped)
        return wrapped
    }

    private fun <T> wrapItem(item: T): RendererItem<T> = RendererItem(item)

    private fun convert(items: List<RendererItem<*>>) =
        PagedList.Builder<Int, RendererItem<*>>(DataSource(ListProvider(items)), DataSource.default(items.size))
            .setInitialKey(0)
            .setNotifyExecutor(Executors.newSingleThreadExecutor())
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()

    private fun setRetry(retry: () -> Unit) {
        this.retryBlock = retry
    }

    fun retry() = retryBlock?.invoke()

    enum class State { INITIAL, DONE, LOADING, ERROR, FINISHED }

    private fun executeService(actualState: State = State.LOADING, service: suspend () -> Unit) {
        state.postValue(actualState)
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                try {
                    service()
                    delay(100)
                    state.postValue(State.DONE)
                } catch (ex: Exception) {
                    state.postValue(State.ERROR)
                }
            }
        }
    }
}