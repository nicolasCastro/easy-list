package com.thinkup.sampleeasy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinkup.easylist.RendererAdapter
import com.thinkup.sampleeasy.databinding.ActivityRendererListBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RendererListActivity : AppCompatActivity() {

    private var binding: ActivityRendererListBinding? = null
    private val adapter = RendererAdapter()
    private val storageSampleData: StorageSampleData by lazy { StorageSampleData(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRendererListBinding.inflate(layoutInflater)
        setContentView(requireNotNull(binding).root)

        prepareList()
        prepareRefresh()
        loadItems()

        GlobalScope.launch {
            delay(5000)
            Log.d("EASY-LIST:::", adapter.getItems().toString())
        }
    }

    private fun prepareList() {
        requireNotNull(binding).sampleList.layoutManager = LinearLayoutManager(this)
        adapter.addRenderer(SampleRenderer())
        requireNotNull(binding).sampleList.adapter = adapter
    }

    private fun prepareRefresh() {
        requireNotNull(binding).sampleRefresh.isRefreshing = false
        requireNotNull(binding).sampleRefresh.setColorSchemeResources(R.color.colorPrimary)
        requireNotNull(binding).sampleRefresh.setOnRefreshListener {
            requireNotNull(binding).sampleRefresh.isRefreshing = true
            loadItems()
        }
    }

    private fun loadItems() {
        adapter.setItems(getInitial())
        requireNotNull(binding).sampleRefresh.isRefreshing = false
    }

    private fun getInitial(): List<SampleItem> = storageSampleData.get(10)
}
