package com.example.androidtraining

class AdviceRepo(private val adviceService: AdviceService) {
    fun getAdvice() = adviceService.getAdvice()
}