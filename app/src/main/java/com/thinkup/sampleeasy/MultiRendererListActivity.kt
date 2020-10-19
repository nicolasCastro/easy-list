package com.thinkup.sampleeasy

import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinkup.easylist.RendererAdapter
import com.thinkup.easylist.removeItemAnimator
import kotlinx.android.synthetic.main.activity_renderer_list.*

class MultiRendererListActivity : AppCompatActivity() {

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
        adapter.addRenderer(ColoredRenderer(this::removeItem))
        adapter.addRenderer(StringRenderer())
        sampleList.adapter = adapter
    }

    private fun removeItem(item: Any) {
        adapter.removeItemAnimator(
            recyclerView = sampleList,
            item = item,
            animation = AnimationUtils.loadAnimation(baseContext, R.anim.fade_out_right)
        )
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

    fun getInitial(): List<Any> {
        return mutableListOf<Any>().apply {

            add(SampleItem("1", "Name1", "Male", "Company1"))
            add(ColoredItem(Color.BLUE))
            add(ColoredItem(Color.GREEN))
            add(SampleItem("2", "Name2", "Female", "Company2"))
            add(SampleItem("3", "Name3", "Male", "Company3"))
            add("It's a String!")
            add(ColoredItem(Color.MAGENTA))
            add("It's a String too!")
        }
    }
}
