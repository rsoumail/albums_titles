package com.rsoumail.mymemories.di

import com.rsoumail.mymemories.data.datasource.NetworkDataSource
import com.rsoumail.mymemories.data.datasource.RemoteMemoriesDataSource
import com.rsoumail.mymemories.data.datasource.SettingsDataSource
import com.rsoumail.mymemories.data.repository.MemoriesRepositoryImpl
import com.rsoumail.mymemories.data.repository.NetworkRepositoryImpl
import com.rsoumail.mymemories.data.repository.SettingsRepositoryImpl
import com.rsoumail.mymemories.domain.repository.MemoriesRepository
import com.rsoumail.mymemories.domain.repository.NetworkRepository
import com.rsoumail.mymemories.domain.repository.SettingsRepository
import com.rsoumail.mymemories.domain.usecase.*
import com.rsoumail.mymemories.framework.MyMemoriesSharedPreferences
import com.rsoumail.mymemories.framework.datasource.NetworkDataSourceImpl
import com.rsoumail.mymemories.framework.datasource.RemoteMemoriesDataSourceImpl
import com.rsoumail.mymemories.framework.datasource.SettingsDataSourceImpl
import com.rsoumail.mymemories.framework.network.RemoteMemoriesService
import com.rsoumail.mymemories.utils.DefaultDispatcherProvider
import com.rsoumail.mymemories.utils.DispatcherProvider
import com.rsoumail.mymemories.utils.REMOTE_MEMORIES_URL
import com.rsoumail.mymemories.utils.createWebService
import com.rsoumail.mymemories.view.viewmodels.MemoriesActivityViewModel
import com.rsoumail.mymemories.view.viewmodels.MemoriesFragmentViewModel
import com.rsoumail.mymemories.view.viewmodels.MemoriesPagingSource
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { MemoriesActivityViewModel() }
    viewModel { MemoriesFragmentViewModel(dispatchers = get(), isFirstLaunchUseCase = get(), isNetworkAvailableUseCase = get() , updateFirstLaunchStatusUseCase = get() , getNetworkStatusNotifier = get(), memoriesPagingSource = get()) }
    single { MemoriesPagingSource(getMemoriesUseCase = get()) }
    factory <DispatcherProvider> { DefaultDispatcherProvider() }
}

val domainModule = module {
    factory { GetMemoriesUseCase(memoriesRepository = get()) }
    factory { GetNetworkStatusNotifier(networkRepository = get()) }
    factory { IsFirstLaunchUseCase(settingsRepository = get()) }
    factory { UpdateFirstLaunchStatusUseCase(settingsRepository = get()) }
    factory { IsNetworkAvailableUseCase(networkRepository = get()) }
}

val dataModule = module {
    single <MemoriesRepository> { MemoriesRepositoryImpl(remoteMemoriesDataSource = get()) }
    single <RemoteMemoriesDataSource> { RemoteMemoriesDataSourceImpl(remoteMemoriesService = get()) }
    single <RemoteMemoriesService> { createWebService(REMOTE_MEMORIES_URL, androidApplication()) }
    single <NetworkDataSource> { NetworkDataSourceImpl(context = androidApplication()) }
    single <NetworkRepository> { NetworkRepositoryImpl(networkDataSource = get()) }
    single <SettingsDataSource> { SettingsDataSourceImpl(myMemoriesSharedPreferences = get()) }
    single <SettingsRepository> { SettingsRepositoryImpl(settingsDataSource = get()) }
    single { MyMemoriesSharedPreferences(androidApplication()) }
}

