package com.example.androidtraining.asynctask

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtraining.R
import com.example.androidtraining.model.DownLoadDatabase
import com.example.androidtraining.model.DownloadModel
import com.example.androidtraining.model.DownloaderDao
import kotlinx.android.synthetic.main.activity_asynctask.*

class AsyncActivity : AppCompatActivity() {

    private var task: DownloadFileTask? = null

    companion object {
        const val SUCCESS = "Download Success"
        const val ERROR = "Download Error"
        const val STOP = "Download Stop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asynctask)
        loadDownLoadTask()
    }

    override fun onDestroy() {
        super.onDestroy()
        task?.cancel(true)
    }

    private fun loadDownLoadTask() {
        startAsync.setOnClickListener {
            buttonDisable(it)
            task = builderAsyncTask(this)
            task?.execute("url1", "url2", "url3")
        }

        stopAsync.setOnClickListener {
            task?.stop()
            task = null
        }

        resumeAsync.setOnClickListener {
            buttonDisable(it)
            if (task != null) task?.cancel(true)

            task = builderAsyncTask(this)
            task?.resumeFlag = true
            task?.execute("url1", "url2", "url3")
        }

        restartAsync.setOnClickListener {
            buttonDisable(it)
            if (task != null) task?.cancel(true)

            task = builderAsyncTask(this)
            task?.execute("url1", "url2", "url3")
        }
    }

    private fun buttonDisable(vararg views: View) {
        views.forEach {
            it.isClickable = false
            it.setBackgroundColor(Color.parseColor("#dcdcda"))
        }
    }

    private fun buttonEnable(vararg views: View) {
        views.forEach {
            it.isClickable = true
            it.setBackgroundColor(Color.parseColor("#03DAC5"))
        }
    }

    private fun builderAsyncTask(context: Context): DownloadFileTask {
        return DownloadFileTask(
            DownLoadDatabase.getDownloadDatabaseInstance(context).downloadDao()
        )
    }

    inner class DownloadFileTask(
        private val dao: DownloaderDao
    ) : AsyncTask<String, Int, String>() {
        internal var resumeFlag: Boolean = false
        private var startDownloadFileIndex = 0
        private var startDownloadProgressIndex = 0
        private var urlIndex = 0
        private var progressIndex = 0
        private var id = 1

        private fun downloadFile(startFileIndex: Int, startProcessIndex: Int): Pair<Int, String> {
            for (i in startProcessIndex..100) {
                this@AsyncActivity.progressBarAsync.progress = i
                publishProgress(i, startFileIndex)
                try {
                    Thread.sleep(i.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    return Pair(i, ERROR)
                }
            }
            return Pair(100, SUCCESS)
        }

        override fun doInBackground(vararg params: String): String {
            var result = ""

            //if resume, update index
            if (resumeFlag) {
                val model = dao.getLastDownloadModel()
                startDownloadFileIndex = model.urlIndex
                startDownloadProgressIndex = model.progress
            }

            for (index in startDownloadFileIndex until params.size) {
                val response = downloadFile(index, startDownloadProgressIndex)
                urlIndex = index
                progressIndex = response.first
                dao.insertNewDownload(
                    DownloadModel(
                        id++,
                        urlIndex,
                        progressIndex,
                        System.currentTimeMillis()
                    )
                )
                result = response.second
                if (isCancelled) break
            }

            return result
        }

        override fun onProgressUpdate(vararg values: Int?) {
            var progress = 0
            var url = 0
            values[0]?.apply {
                progress = this
            }
            values[1]?.apply {
                url = this
            }
            textViewAsync.text = "Download Progress for url_$url：$progress %"
        }

        override fun onPostExecute(result: String?) {
            Toast.makeText(this@AsyncActivity, "$result", Toast.LENGTH_LONG).show()
            title = "$result"
            cancel(true)
            buttonEnable(startAsync, restartAsync, resumeAsync)
        }

        override fun onCancelled() {
            super.onCancelled()
            Toast.makeText(this@AsyncActivity, "$STOP", Toast.LENGTH_LONG).show()
        }

        internal fun stop() {
            cancel(true)
            dao.insertNewDownload(
                DownloadModel(
                    id++,
                    urlIndex,
                    progressIndex,
                    System.currentTimeMillis()
                )
            )
            buttonEnable(restartAsync, resumeAsync)
        }
    }
}
