package com.rsoumail.mymemories.domain.usecase

import com.rsoumail.mymemories.domain.repository.SettingsRepository

class IsFirstLaunchUseCase(private val settingsRepository: SettingsRepository) {

    operator fun invoke() = settingsRepository.isFirstLaunch()
}