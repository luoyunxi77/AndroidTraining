package com.example.androidtraining

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidtraining.model.Advice
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomViewModel(private val adviceRepo: AdviceRepo) : ViewModel() {
    private val _result: MutableLiveData<String> = MutableLiveData()

    val result: LiveData<String> = _result

    fun load() {
        adviceRepo.getAdvice().enqueue(object : Callback<Advice> {
            override fun onResponse(call: Call<Advice>, response: Response<Advice>) {
                if (response.isSuccessful) {
                    _result.value = response.body()!!.slip.advice
                }
            }

            override fun onFailure(call: Call<Advice>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}