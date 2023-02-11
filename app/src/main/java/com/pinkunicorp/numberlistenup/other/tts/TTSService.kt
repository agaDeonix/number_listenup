package com.pinkunicorp.numberlistenup.other.tts

import android.app.Activity

interface TTSService {
    fun init(activity: Activity)
    fun speak(text: String)
    fun stop()
    fun shutdown()
}