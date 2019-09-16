package com.thinkup.easypagedlist

import com.google.gson.Gson
import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader


class StorageSampleData(context: Context) {
    private val items: List<SampleItem>

    init {
        val list: List<SampleItem> = listOf()
        items = try {
            val stream = context.getResources().openRawResource(R.raw.items)
            val reader = BufferedReader(InputStreamReader(stream))
            Gson().fromJson(reader, list::class.java)
        } catch (ex: Exception) {
            list
        }
    }

    fun get(limit: Int = 0, offset: Int = 0): List<SampleItem> = items.subList(offset, limit)

}