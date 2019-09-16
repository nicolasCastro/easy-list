package com.thinkup.easypagedlist

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

inline fun <reified T> getType(): Type = object : TypeToken<List<T>>() {}.type