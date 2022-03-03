package com.rsoumail.mymemories.di

import com.rsoumail.mymemories.framework.network.RemoteMemoriesService
import com.rsoumail.mymemories.utils.REMOTE_MEMORIES_URL
import com.rsoumail.mymemories.utils.createWebService
import org.koin.dsl.module


val appModule = module {
    single <RemoteMemoriesService> { createWebService(REMOTE_MEMORIES_URL) }
}