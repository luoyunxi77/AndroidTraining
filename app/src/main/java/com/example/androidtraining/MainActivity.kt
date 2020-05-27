package com.example.androidtraining

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidtraining.dagger.CustomApplication
import com.example.androidtraining.dagger.CustomViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    companion object {
        const val SUCCESS = 1
        const val ERROR = 0
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: CustomViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as CustomApplication).customComponent.inject(this)

        loadView()
    }

    private fun loadView() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.load()
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.result.observe(this, Observer {
            if (it.first == SUCCESS) {
                response.text = it.second
            } else {
                response.visibility = View.GONE
                Toast.makeText(this, "${it.second}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
