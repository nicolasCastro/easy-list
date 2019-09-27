package com.thinkup.sampleeasy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinkup.easylist.RendererAdapter
import kotlinx.android.synthetic.main.activity_renderer_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RendererListActivity : AppCompatActivity() {

    val adapter = RendererAdapter()
    private val storageSampleData: StorageSampleData by lazy { StorageSampleData(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_renderer_list)

        prepareList()
        prepareRefresh()
        loadItems()

        GlobalScope.launch {
            delay(5000)
            Log.d("EASY-LIST:::", adapter.getItems().toString())
        }
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
