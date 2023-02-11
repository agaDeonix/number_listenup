package com.pinkunicorp.numberlistenup.other

import android.app.Application
import com.pinkunicorp.numberlistenup.data.repository.PrefScoreRepositoryImpl
import com.pinkunicorp.numberlistenup.other.tts.TTSService
import com.pinkunicorp.numberlistenup.other.tts.TTSServiceImpl
import com.pinkunicorp.numberlistenup.data.repository.PrefSettingsRepositoryImpl
import com.pinkunicorp.numberlistenup.data.repository.ScoreRepository
import com.pinkunicorp.numberlistenup.data.repository.SettingsRepository
import com.pinkunicorp.numberlistenup.ui.screens.home.HomeViewModel
import com.pinkunicorp.numberlistenup.ui.screens.settings.SettingsViewModel
import com.pinkunicorp.numberlistenup.ui.screens.training.TrainingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    private val appModule = module {
        single<TTSService> { TTSServiceImpl() }
        single<SettingsRepository> { PrefSettingsRepositoryImpl(androidContext()) }
        single<ScoreRepository> { PrefScoreRepositoryImpl(androidContext()) }
    }

    private val viewModelModule = module {
        viewModel { HomeViewModel(get()) }
        viewModel { SettingsViewModel(get()) }
        viewModel { TrainingViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(appModule, viewModelModule)
        }
    }
}