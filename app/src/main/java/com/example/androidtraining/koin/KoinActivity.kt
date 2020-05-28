package com.example.androidtraining.koin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.androidtraining.CustomViewModel
import com.example.androidtraining.MainActivity.Companion.SUCCESS
import com.example.androidtraining.R
import kotlinx.android.synthetic.main.activity_koin.*
import org.koin.android.viewmodel.ext.android.viewModel

class KoinActivity : AppCompatActivity() {

    private val customViewModel: CustomViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_koin)

        loadView()
    }

    private fun loadView() {
        swipeRefreshLayout_Koin.setColorSchemeResources(android.R.color.holo_blue_bright)

        swipeRefreshLayout_Koin.setOnRefreshListener {
            customViewModel.load()
            swipeRefreshLayout_Koin.isRefreshing = false
        }

        customViewModel.result.observe(this, Observer {
            if (it.first == SUCCESS) {
                responseKoin.text = it.second
            } else {
                responseKoin.visibility = View.GONE
                Toast.makeText(this, "${it.second}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
