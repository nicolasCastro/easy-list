package com.thinkup.sampleeasy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thinkup.sampleeasy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(requireNotNull(binding).root)
        with(binding) {
            this?.let {
                it.pagedSampleButton.setOnClickListener {
                    startActivity(Intent(this@MainActivity, PagedListActivity::class.java))
                }

                it.listSampleButton.setOnClickListener {
                    startActivity(Intent(this@MainActivity, RendererListActivity::class.java))
                }

                it.multilistSampleButton.setOnClickListener {
                    startActivity(Intent(this@MainActivity, MultiRendererListActivity::class.java))
                }
            }
        }
    }
}
