package com.rsoumail.mymemories.domain.usecase

import com.rsoumail.mymemories.domain.repository.NetworkRepository

class IsNetworkAvailableUseCase(private val networkRepository: NetworkRepository) {

    operator fun invoke() = networkRepository.isNetworkAvailable()
}