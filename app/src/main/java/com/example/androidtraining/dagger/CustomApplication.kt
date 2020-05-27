package com.example.androidtraining.dagger

import android.app.Application

class CustomApplication : Application() {

    lateinit var customComponent: CustomComponent

    override fun onCreate() {
        super.onCreate()
        customComponent = DaggerCustomComponent.create()
    }
}