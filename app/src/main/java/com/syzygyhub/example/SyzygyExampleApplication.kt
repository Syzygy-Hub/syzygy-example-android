package com.syzygyhub.example

import android.app.Application
import com.syzygyhub.example.di.AppModule

class SyzygyExampleApplication : Application() {
    lateinit var appModule: AppModule
        private set

    override fun onCreate() {
        super.onCreate()
        appModule = AppModule(this)
        appModule.setup()
    }
}
