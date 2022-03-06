package com.rsoumail.mymemories.data.datasource

interface SettingsDataSource {

    fun isFirstLaunch(): Boolean

    fun updateFirstLaunch()
}