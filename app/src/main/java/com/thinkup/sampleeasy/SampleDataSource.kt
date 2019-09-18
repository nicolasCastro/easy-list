package com.thinkup.sampleeasy

import android.content.Context
import com.thinkup.easycore.RendererItem
import com.thinkup.easypagedlist.core.PagedViewModel
import com.thinkup.easypagedlist.core.RendererDataSource

class SampleDataSource(
    private val context: Context,
    private val pageSize: Int = PagedViewModel.DEFAULT_PAGE_SIZE,
    inputSource: MutableList<RendererItem<*>> = mutableListOf(),
    firstInstance: Boolean = true
) : RendererDataSource<SampleDataSource>(inputSource, firstInstance) {

    private var actualOffset = 0
    private val storageSampleData = StorageSampleData(context)

    override fun getKeys(items: List<*>) {
        actualOffset++
    }

    override suspend fun getInitial(): List<*> = storageSampleData.get(pageSize)

    override suspend fun getAfter(): List<*> = storageSampleData.get(pageSize, actualOffset * pageSize)

    override suspend fun getBefore(): List<*> = storageSampleData.get(pageSize, actualOffset * pageSize)

    override fun create(inputSource: MutableList<RendererItem<*>>, firstInstance: Boolean): SampleDataSource =
        SampleDataSource(context, pageSize, inputSource, firstInstance)
}