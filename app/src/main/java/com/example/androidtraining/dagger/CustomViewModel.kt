package com.example.androidtraining.dagger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidtraining.AdviceRepo

class CustomViewModel(private val adviceRepo: AdviceRepo) : ViewModel() {
    private val _result: MutableLiveData<Pair<Int,String>> = MutableLiveData()

    val result: LiveData<Pair<Int,String>> = _result

    fun load() {
        adviceRepo.load {
            _result.value = it
        }
    }
}