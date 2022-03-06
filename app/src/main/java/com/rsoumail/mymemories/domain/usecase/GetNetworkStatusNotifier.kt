package com.rsoumail.mymemories.domain.usecase

import com.rsoumail.mymemories.domain.repository.NetworkRepository

class GetNetworkStatusNotifier(private val networkRepository: NetworkRepository) {

    suspend operator fun invoke() = networkRepository.getNetworkStatusNotifier()
}