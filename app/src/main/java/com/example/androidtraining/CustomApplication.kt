package com.example.androidtraining

import android.app.Application
import com.example.androidtraining.dagger.CustomComponent
import com.example.androidtraining.dagger.DaggerCustomComponent
import com.example.androidtraining.koin.koinCustomModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CustomApplication : Application() {

    lateinit var customComponent: CustomComponent

    override fun onCreate() {
        super.onCreate()
        customComponent = DaggerCustomComponent.create()

        startKoin {
            androidLogger()
            androidContext(this@CustomApplication)
            modules(koinCustomModule)
        }
    }
}
