package com.example.androidtraining.dagger

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidtraining.CustomApplication
import com.example.androidtraining.CustomViewModel
import com.example.androidtraining.MainActivity.Companion.SUCCESS
import com.example.androidtraining.R
import kotlinx.android.synthetic.main.activity_dagger.*
import javax.inject.Inject

class DaggerActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: CustomViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagger)

        (application as CustomApplication).customComponent.inject(this)

        loadView()
    }

    private fun loadView() {
        swipeRefreshLayout_Dagger.setColorSchemeResources(android.R.color.holo_blue_bright)

        swipeRefreshLayout_Dagger.setOnRefreshListener {
            viewModel.load()
            swipeRefreshLayout_Dagger.isRefreshing = false
        }

        viewModel.result.observe(this, Observer {
            if (it.first == SUCCESS) {
                responseDagger.text = it.second
            } else {
                responseDagger.visibility = View.GONE
                Toast.makeText(this, "${it.second}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
