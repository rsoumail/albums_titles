package com.rsoumail.mymemories.framework.datasource

import com.rsoumail.mymemories.data.datasource.SettingsDataSource
import com.rsoumail.mymemories.framework.MyMemoriesSharedPreferences

class SettingsDataSourceImpl(private val myMemoriesSharedPreferences: MyMemoriesSharedPreferences): SettingsDataSource {

    override fun isFirstLaunch() = myMemoriesSharedPreferences.firstLaunch

    override fun updateFirstLaunch() { myMemoriesSharedPreferences.firstLaunch = false }
}