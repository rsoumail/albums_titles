package com.rsoumail.mymemories.domain.usecase

import com.rsoumail.mymemories.domain.repository.MemoriesRepository

class GetMemoriesUseCase(private val memoriesRepository: MemoriesRepository) {

    suspend operator fun invoke() = memoriesRepository.getMemories()
}