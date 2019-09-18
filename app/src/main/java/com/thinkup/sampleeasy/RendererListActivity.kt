package com.thinkup.sampleeasy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinkup.easylist.RendererAdapter
import kotlinx.android.synthetic.main.activity_renderer_list.*

class RendererListActivity : AppCompatActivity() {

    val adapter = RendererAdapter()
    private val storageSampleData: StorageSampleData by lazy { StorageSampleData(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_renderer_list)

        prepareList()
        prepareRefresh()
        loadItems()
    }

    private fun prepareList() {
        sampleList.layoutManager = LinearLayoutManager(this)
        adapter.addRenderer(SampleRenderer())
        sampleList.adapter = adapter
    }

    private fun prepareRefresh() {
        sampleRefresh.isRefreshing = false
        sampleRefresh.setColorSchemeResources(R.color.colorPrimary)
        sampleRefresh.setOnRefreshListener {
            sampleRefresh.isRefreshing = true
            loadItems()
        }
    }

    private fun loadItems() {
        adapter.setItems(getInitial())
        sampleRefresh.isRefreshing = false
    }

    fun getInitial(): List<SampleItem> = storageSampleData.get(10)
}
