package com.rsoumail.mymemories.data.repository

import com.rsoumail.mymemories.data.datasource.SettingsDataSource
import com.rsoumail.mymemories.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val settingsDataSource: SettingsDataSource): SettingsRepository{
    override fun isFirstLaunch() = settingsDataSource.isFirstLaunch()

    override fun updateFirstLaunch() = settingsDataSource.updateFirstLaunch()
}