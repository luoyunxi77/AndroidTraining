package com.example.androidtraining

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtraining.dagger.DaggerActivity
import com.example.androidtraining.koin.KoinActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val SUCCESS = 1
        const val ERROR = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadOtherActivity()
    }

    private fun loadOtherActivity() {
        daggerButton.setOnClickListener {
            val intent = Intent(this, DaggerActivity::class.java)
            startActivity(intent)
        }

        koinButton.setOnClickListener {
            val intent = Intent(this, KoinActivity::class.java)
            startActivity(intent)
        }
    }
}
