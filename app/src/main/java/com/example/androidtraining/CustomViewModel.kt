package com.example.androidtraining

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomViewModel(private val adviceRepo: AdviceRepo) : ViewModel() {
    private val _result: MutableLiveData<Pair<Int,String>> = MutableLiveData()

    val result: LiveData<Pair<Int,String>> = _result

    fun load() {
        adviceRepo.load {
            _result.value = it
        }
    }
}