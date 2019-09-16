package com.thinkup.easypagedlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thinkup.easypagedlist.core.PagedViewModel

class SampleViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val dataSource = SampleDataSource(context)
        return PagedViewModel<SampleDataSource>(dataSource) as T
    }
}