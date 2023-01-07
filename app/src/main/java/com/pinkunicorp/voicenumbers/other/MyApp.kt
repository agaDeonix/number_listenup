package com.pinkunicorp.voicenumbers.other

import android.app.Application
import com.pinkunicorp.voicenumbers.other.tts.TTSService
import com.pinkunicorp.voicenumbers.other.tts.TTSServiceImpl
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    val appModule = module {
        single<TTSService> { TTSServiceImpl() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            modules(appModule)
        }
    }
}