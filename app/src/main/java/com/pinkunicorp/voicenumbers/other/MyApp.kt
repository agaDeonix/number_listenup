package com.pinkunicorp.voicenumbers.other

import android.app.Application
import com.pinkunicorp.voicenumbers.other.tts.TTSService
import com.pinkunicorp.voicenumbers.other.tts.TTSServiceImpl
import com.pinkunicorp.voicenumbers.data.repository.PrefSettingsRepositoryImpl
import com.pinkunicorp.voicenumbers.data.repository.SettingsRepository
import com.pinkunicorp.voicenumbers.ui.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    private val appModule = module {
        single<TTSService> { TTSServiceImpl() }
        single<SettingsRepository> { PrefSettingsRepositoryImpl(androidContext()) }
    }

    private val viewModelModule = module {
        viewModel { SettingsViewModel(get()) }
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