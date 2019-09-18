package com.thinkup.sampleeasy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinkup.easypagedlist.core.PagedViewModel
import com.thinkup.easypagedlist.core.RendererDataSource
import kotlinx.android.synthetic.main.activity_paged_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class PagedListActivity : AppCompatActivity() {

    private val paged: PagedViewModel<SampleDataSource> by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paged_list)

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
            sampleRefresh.isRefreshing = false
        }
    }

    private fun prepareList() {
        sampleList.layoutManager = LinearLayoutManager(this)
        paged.addRenderers(SampleRenderer())
        paged.setFooterLayout(R.layout.footer_loading, R.layout.footer_error)
        paged.attachToRecyclerView(sampleList)
    }

    private fun prepareRefresh() {
        sampleRefresh.isRefreshing = false
        sampleRefresh.setColorSchemeResources(R.color.colorPrimary)
        sampleRefresh.setOnRefreshListener {
            paged.refresh()
        }
    }
}
