package com.example.pasteleriamilssaboresandroid

import android.app.Application
import com.example.pasteleriamilssaboresandroid.di.AppContainer
import com.example.pasteleriamilssaboresandroid.di.DefaultAppContainer

class PasteleriaMilSaboresApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
