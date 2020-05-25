package com.example.androidtraining.handlerthread

import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtraining.R
import kotlinx.android.synthetic.main.activity_handlerthread.*

class HandlerActivity : AppCompatActivity() {


    private lateinit var handlerThread: HandlerThread
    private lateinit var workHandler: Handler
    private var mainHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val pair = msg.obj as Pair<String, Int>
            progressBarThread.progress = pair.second
            textViewThread.text = "Download Progress for ${pair.first}：${pair.second} %"
        }
    }

    companion object {
        const val SUCCESS = "Download Success"
        const val ERROR = "Download Error"
        const val STOP = "Download Stop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handlerthread)

        initHandlerThread()
        loadView()
    }

    private fun loadView() {
        startThread.setOnClickListener {
            if (handlerThread.isAlive) {
                sendMessage()
            }
        }

        stopThread.setOnClickListener {
            if (handlerThread.isAlive) {
                handlerThread.interrupt()
                handlerThread.quitSafely()
            }
            Toast.makeText(this@HandlerActivity, "$STOP", Toast.LENGTH_LONG).show()
        }

        restartThread.setOnClickListener {
            if (handlerThread.isAlive) {
                handlerThread.quitSafely()
            } else {
                initHandlerThread()
                sendMessage()
            }
        }
    }

    private fun initHandlerThread() {
        handlerThread = HandlerThread("DownLoad Thread")
        handlerThread.start()
        workHandler = object : Handler(handlerThread.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val urls = msg.obj as Array<String>
                for (url in urls) {
                    try {
                        downloadFile(url)
                    } catch (e: InterruptedException) {
                        break
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        val message = Message()
        message.obj = arrayOf("url1", "url2", "url3")
        workHandler.sendMessage(message)
    }

    private fun downloadFile(url: String): String {
        for (i in 0..100) {
            val message = Message()
            message.obj = Pair(url, i)
            mainHandler.sendMessage(message)

            try {
                Thread.sleep(i.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
                throw e
            }
        }
        return SUCCESS
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacksAndMessages(null)
        workHandler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
    }
}
