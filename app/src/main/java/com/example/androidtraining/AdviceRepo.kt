package com.example.androidtraining

import android.os.Handler
import android.os.Looper
import com.example.androidtraining.MainActivity.Companion.ERROR
import com.example.androidtraining.MainActivity.Companion.SUCCESS
import com.example.androidtraining.model.Advice
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdviceRepo(private val adviceService: AdviceService) {

    private lateinit var response: ((Pair<Int, String>) -> Unit)

    private val handler = Handler(Looper.getMainLooper()) {
        response.invoke(Pair(it.what, it.obj as String))
        true
    }

    fun load(response: (Pair<Int, String>) -> Unit) {
        this.response = response
        val message = handler.obtainMessage()

        adviceService.getAdvice().enqueue(object : Callback<Advice> {
            override fun onResponse(call: Call<Advice>, response: Response<Advice>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        message.what = SUCCESS
                        message.obj = it.slip.advice
                        message.sendToTarget()
                    }
                }
            }

            override fun onFailure(call: Call<Advice>, t: Throwable) {
                message.what = ERROR
                message.obj = t
                message.sendToTarget()
            }
        })
    }
}