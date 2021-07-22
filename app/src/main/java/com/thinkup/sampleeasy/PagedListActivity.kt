package com.thinkup.sampleeasy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinkup.easypagedlist.core.FilterManager
import com.thinkup.easypagedlist.core.PagedViewModel
import com.thinkup.easypagedlist.core.RendererDataSource
import com.thinkup.sampleeasy.databinding.ActivityPagedListBinding
import org.koin.android.viewmodel.ext.android.viewModel

class PagedListActivity : AppCompatActivity() {

    private var binding: ActivityPagedListBinding? = null
    private val paged: PagedViewModel<SampleDataSource> by viewModel()
    private val filterManager = Filters()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagedListBinding.inflate(layoutInflater)
        setContentView(requireNotNull(binding).root)

        prepareRefresh()
        prepareList()
        initState()
        initList()
    }

    private fun initState() {
        paged.initState(this) { empty, state ->
            if (empty) {
                when (state) {
                    RendererDataSource.State.ERROR ->
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    RendererDataSource.State.DONE ->
                        Toast.makeText(this, "No items", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initList() {
        paged.initList(this) {
            requireNotNull(binding).sampleRefresh.isRefreshing = false
        }
    }

    private fun prepareList() {
        requireNotNull(binding).sampleList.layoutManager = LinearLayoutManager(this)
        paged.addRenderers(SampleRenderer())
//        paged.setFooterBinding(FooterLoadingBinding.inflate(layoutInflater), FooterErrorBinding.inflate(layoutInflater))
        paged.attachToRecyclerView(requireNotNull(binding).sampleList)
    }

    private fun prepareRefresh() {
        requireNotNull(binding).sampleRefresh.isRefreshing = false
        requireNotNull(binding).sampleRefresh.setColorSchemeResources(R.color.colorPrimary)
        requireNotNull(binding).sampleRefresh.setOnRefreshListener {
            paged.updateFilter(filterManager)
            paged.refresh()
        }
    }

    class Filters : FilterManager<String> {
        private var text = ""
        override fun clear() {
            text = ""
        }

        override fun getFilter(): String {
            return text
        }

        override fun setFilter(new: String) {
            text = new
        }

    }
}
