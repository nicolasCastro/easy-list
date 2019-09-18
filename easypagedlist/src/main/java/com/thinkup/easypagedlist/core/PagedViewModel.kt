package com.thinkup.easypagedlist.core

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.thinkup.easypagedlist.R
import com.thinkup.easycore.RendererItem

class PagedViewModel<T : RendererDataSource<T>>(dataSource: T, pageSize: Int = DEFAULT_PAGE_SIZE) :
    ViewModel(), RendererPagedAdapter.RetryCallback {

    private val liveItemsSource: LiveData<PagedList<RendererItem<*>>>
    private val factory: RendererDataSourceFactory<T> = RendererDataSourceFactory(dataSource)
    private val adapter: RendererPagedAdapter =
        RendererPagedAdapter(this)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        liveItemsSource = LivePagedListBuilder(factory, config).build()
    }

    fun initState(owner: LifecycleOwner, update: ((Boolean, RendererDataSource.State) -> Unit)?) {
        getState().observe(owner, Observer { state ->
            if (!listIsEmpty()) {
                adapter.setState(state ?: RendererDataSource.State.DONE)
            }
            update?.invoke(listIsEmpty(), state)
        })
    }

    fun initList(owner: LifecycleOwner, update: (() -> Unit)?) {
        liveItemsSource.observe(owner, Observer {
            adapter.submitList(it)
            update?.invoke()
        })
    }

    fun addRenderers(vararg renderers: com.thinkup.easycore.ViewRenderer<out Any, out View>) {
        renderers.forEach {
            adapter.addRenderer(it)
        }
    }

    fun setFooterLayout(@LayoutRes loading: Int = R.layout.footer_loading, @LayoutRes error: Int = R.layout.footer_error) =
        adapter.setFooterLayout(loading, error)

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = adapter
    }

    private fun getState(): LiveData<RendererDataSource.State> = Transformations.switchMap<T,
            RendererDataSource.State>(factory.dataSourceLiveData, RendererDataSource<T>::state)

    private fun listIsEmpty() = liveItemsSource.value?.isEmpty() ?: true

    private fun retry() = factory.dataSourceLiveData.value?.retry()

    fun remove(position: Int) = factory.remove(position)

    fun getItem(position: Int) = adapter.get(position)

    fun refresh() = factory.invalidate()

    override fun onError() {
        retry()
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }
}