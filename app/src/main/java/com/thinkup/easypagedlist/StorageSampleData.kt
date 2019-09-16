package com.thinkup.easypagedlist

import com.google.gson.Gson
import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader


class StorageSampleData(context: Context) {
    private val items: MutableList<SampleItem> = mutableListOf()

    init {
        try {
            val stream = context.resources.openRawResource(R.raw.items)
            val reader = BufferedReader(InputStreamReader(stream))
            val data = Gson().fromJson(reader, SampleData::class.java)
            items.addAll(data.data)
        } catch (ex: Exception) {
        }
    }

    fun get(limit: Int = 0, offset: Int = 0): List<SampleItem> = items.subList(offset, limit)
}