package com.thinkup.easypagedlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.thinkup.easypagedlist.core.PagedViewModel
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.ViewModelProviders
import sun.jvm.hotspot.utilities.IntArray


class MainActivity : AppCompatActivity() {

    private lateinit var paged: PagedViewModel<SampleDataSource>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = SampleViewModelFactory(this.application)
        paged = ViewModelProviders.of(this, factory)
            .get(paged::class.java)
    }
}
