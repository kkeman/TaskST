package com.service.codingtest.manager

import android.app.Application
import com.service.codingtest.module.AppModule
import org.koin.core.context.startKoin

class PuzzleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(AppModule)
        }
    }
}