package com.rsoumail.mymemories.domain.repository

interface SettingsRepository {

    fun isFirstLaunch(): Boolean

    fun updateFirstLaunch()
}