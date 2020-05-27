package com.example.androidtraining.dagger

import com.example.androidtraining.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CustomModule::class])
interface CustomComponent {
    fun inject(activity: MainActivity)
}