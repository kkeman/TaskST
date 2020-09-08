package com.service.codingtest.manager

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.service.codingtest.module.AppModule
import org.koin.core.context.startKoin

class PuzzleApp : Application() {

    lateinit var context: Context

    init{
        instance = this
    }

    companion object {
        private var instance: PuzzleApp? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(AppModule)
        }
    }
}