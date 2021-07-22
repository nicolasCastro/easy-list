package com.thinkup.sampleeasy

import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinkup.easylist.RendererAdapter
import com.thinkup.easylist.removeItemAnimator
import com.thinkup.sampleeasy.databinding.ActivityRendererListBinding

class MultiRendererListActivity : AppCompatActivity() {

    private var binding: ActivityRendererListBinding? = null
    private val adapter = RendererAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRendererListBinding.inflate(layoutInflater)
        setContentView(requireNotNull(binding).root)
        prepareList()
        prepareRefresh()
        loadItems()
    }

    private fun prepareList() {
        binding?.sampleList?.layoutManager = LinearLayoutManager(this)
        adapter.addRenderer(SampleRenderer())
        adapter.addRenderer(ColoredRenderer(this::removeItem))
        adapter.addRenderer(StringRenderer())
        binding?.sampleList?.adapter = adapter
    }

    private fun removeItem(item: Any) {
        adapter.removeItemAnimator(
            recyclerView = requireNotNull(binding).sampleList,
            item = item,
            animation = AnimationUtils.loadAnimation(baseContext, R.anim.fade_out_right)
        )
    }

    private fun prepareRefresh() {
        with(binding) {
            this?.let {
                it.sampleRefresh.isRefreshing = false
                it.sampleRefresh.setColorSchemeResources(R.color.colorPrimary)
                it.sampleRefresh.setOnRefreshListener {
                    it.sampleRefresh.isRefreshing = true
                    loadItems()
                }
            }
        }
    }

    private fun loadItems() {
        adapter.setItems(getInitial())
        binding?.sampleRefresh?.isRefreshing = false
    }

    private fun getInitial(): List<Any> {
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
