package com.rsoumail.mymemories.framework

import android.content.Context
import android.content.SharedPreferences
import com.rsoumail.mymemories.utils.FIRST_LAUNCH


class MyMemoriesSharedPreferences(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences("MyMemories", Context.MODE_PRIVATE)

    var firstLaunch: Boolean
        get() = preferences.getBoolean(FIRST_LAUNCH, true)
        set(value) = preferences.edit().putBoolean(FIRST_LAUNCH, value).apply()
}