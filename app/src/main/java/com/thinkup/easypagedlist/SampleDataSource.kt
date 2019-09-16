package com.thinkup.easypagedlist

import android.content.Context
import com.thinkup.easypagedlist.core.PagedViewModel
import com.thinkup.easypagedlist.core.RendererDataSource
import com.thinkup.easypagedlist.core.adapter.RendererItem

class SampleDataSource(
    private val context: Context,
    private val pageSize: Int = PagedViewModel.DEFAULT_PAGE_SIZE,
    inputSource: MutableList<RendererItem<*>> = mutableListOf(),
    firstInstance: Boolean= true
) : RendererDataSource<SampleDataSource>(inputSource, firstInstance) {

    private var actualOffset = 0
    private val storageSampleData = StorageSampleData(context)

    override fun getKeys(items: List<*>) {
        actualOffset = items.size
    }

    override suspend fun getInitial(): List<*> = storageSampleData.get()

    override suspend fun getAfter(): List<*> = storageSampleData.get(pageSize, actualOffset)

    override suspend fun getBefore(): List<*> = storageSampleData.get(pageSize, actualOffset)

    override fun create(inputSource: MutableList<RendererItem<*>>, firstInstance: Boolean): SampleDataSource =
        SampleDataSource(context, pageSize, inputSource, firstInstance)
}