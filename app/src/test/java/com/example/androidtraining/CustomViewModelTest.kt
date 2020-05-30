package com.example.androidtraining

import android.os.Looper
import com.example.androidtraining.model.Advice
import com.example.androidtraining.model.Slip
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.mock.Calls

class CustomViewModelTest {

    private var adviceRepo: AdviceRepo = mockk()

    private val customViewModel = CustomViewModel(adviceRepo)
    private val advice = Advice(Slip(1, "response"))
    private val call: Call<Advice> = Calls.response(advice)

    @Before
    fun setup() {
        every { adviceRepo.getAdvice() } returns call
        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk()
        every { Looper.getMainLooper().thread } returns Thread.currentThread()
    }


    @Test
    fun `should return value when callback is successful`() {
        customViewModel.load()
        assert(customViewModel.result.value == "response")
    }
}
