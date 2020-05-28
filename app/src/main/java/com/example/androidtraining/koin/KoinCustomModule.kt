package com.example.androidtraining.koin

import com.example.androidtraining.AdviceRepo
import com.example.androidtraining.AdviceService
import com.example.androidtraining.CustomViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val koinCustomModule = module {
    single { AdviceRepo(get()) }
    single<AdviceService> {
        Retrofit.Builder().baseUrl("https://api.adviceslip.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AdviceService::class.java)
    }
    viewModel {
        CustomViewModel(get())
    }
}
