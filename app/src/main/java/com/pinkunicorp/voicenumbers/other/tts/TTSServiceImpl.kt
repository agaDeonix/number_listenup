package com.pinkunicorp.voicenumbers.other.tts

import android.app.Activity
import net.gotev.speech.Speech

class TTSServiceImpl : TTSService {
    override fun init(activity: Activity) {
        Speech.init(activity, activity.packageName)
    }

    override fun speak(text: String) {
        Speech.getInstance().say(text)
    }

    override fun stop() {
        Speech.getInstance().stopTextToSpeech()
    }

    override fun shutdown() {
        Speech.getInstance().shutdown()
    }
}