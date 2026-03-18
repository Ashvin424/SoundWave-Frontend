package com.ashvinprajapati.soundwave

import android.app.Application

class SoundWaveApp : Application() {

    companion object {
        lateinit var instance: SoundWaveApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}