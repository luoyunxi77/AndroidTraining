package com.example.androidtraining.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidtraining.AdviceRepo
import com.example.androidtraining.AdviceService
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
class CustomModule {

    @Singleton
    @Provides
    fun provideAdviceService(): AdviceService =
        Retrofit.Builder().baseUrl("https://api.adviceslip.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AdviceService::class.java)

    @Singleton
    @Provides
    fun providesAdviceRepo(service: AdviceService) = AdviceRepo(service)

    @IntoMap
    @ViewModelKey(CustomViewModel::class)
    @Provides
    fun providesViewModel(adviceRepo: AdviceRepo): ViewModel =
        CustomViewModel(adviceRepo)

    @Provides
    @Singleton
    fun providersViewModelFactory(viewModelProviderMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelProviderMap[modelClass]?.get() as T
                    ?: throw IllegalStateException("Unsupported ViewModel")
            }
        }
}