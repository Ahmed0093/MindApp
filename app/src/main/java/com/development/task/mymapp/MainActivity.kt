package com.development.task.mymapp

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.development.task.dmanager.Downloader
import com.development.task.dmanager.base.OnDownloadListener
import com.development.task.dmanager.callback.ContentServiceObserver
import com.development.task.dmanager.models.ServiceContentTypeDownload
import com.development.task.dmanager.models.ServiceJsonDownload
import com.google.gson.Gson
import com.development.task.mymapp.model.ResultRespone
import com.mindvalley.appcontentloader.utilities.ServiceContentDownloadManager
import com.mindvalley.appcontentloader.utilities.downloadManagerProvider
import java.io.File
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {
   private var downloader: Downloader? = null
    private val handler: Handler = Handler()
    private val TAG: String = this::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Respone","Start1")

        fetchUsersFromServer()

    }
    private fun fetchUsersFromServer() {

        val mDataTypeJson = ServiceJsonDownload(Constants.API_URL, object : ContentServiceObserver {
            override fun onStart(mDownloadDataType: ServiceContentTypeDownload) {
                Log.d("Respone","Start")

            }

            override fun onSuccess(mDownloadDataType: ServiceContentTypeDownload) {
                val response = mDownloadDataType.data?.let { String(it, StandardCharsets.UTF_8) }
                val detailsResponses = Gson().fromJson<Array<ResultRespone>>(response, Array<ResultRespone>::class.java)
                Log.d("Respone",detailsResponses.toString())

//                if (detailsResponses.size != 0) {
//                    users.clear()
//                    Collections.addAll<MasterDetails>(users, *detailsResponses)
//                }
//                val handler = Handler()
//                handler.postDelayed({
//                    if (listingView != null) {
//                        listingView.renderUserList(users)
//                    }
//                }, context.getResources().getInteger(android.R.integer.config_mediumAnimTime).toLong())

            }

            override fun onFailure(
                mDownloadDataType: ServiceContentTypeDownload,
                statusCode: Int,
                errorResponse: ByteArray?,
                e: Throwable?
            ) {
                Log.d("Respone","failure"+statusCode)

            }

            override fun onRetry(mDownloadDataType: ServiceContentTypeDownload, retryNo: Int) {
                Log.d("Respone","Retry")
            }
        })
        downloadManagerProvider.getRequest(mDataTypeJson)
    }
    private fun getDownloader() {
        downloader = Downloader.Builder(
            this,
            "https://s3-us-west-2.amazonaws.com/uw-s3-cdn/wp-content/uploads/sites/6/2017/11/04133712/waterfall.jpg"
        ).downloadListener(object : OnDownloadListener {
            override fun onStart() {
               // handler.post { current_status_txt.text = "onStart" }
                Log.d(TAG, "onStart")
            }

            override fun onPause() {
               // handler.post { current_status_txt.text = "onPause" }
                Log.d(TAG, "onPause")
            }

            override fun onResume() {
                //handler.post { current_status_txt.text = "onResume" }
                Log.d(TAG, "onResume")
            }

            override fun onProgressUpdate(percent: Int, downloadedSize: Int, totalSize: Int) {
//                handler.post {
//                    current_status_txt.text = "onProgressUpdate"
//                    percent_txt.text = percent.toString().plus("%")
//                    size_txt.text = getSize(downloadedSize)
//                    total_size_txt.text = getSize(totalSize)
//                    download_progress.progress = percent
//                }
                Log.d(
                    TAG,
                    "onProgressUpdate: percent --> $percent downloadedSize --> $downloadedSize totalSize --> $totalSize "
                )
            }

            override fun onCompleted(file: File?) {
           //     handler.post { current_status_txt.text = "onCompleted" }
                Log.d(TAG, "onCompleted: file --> $file")
            }

            override fun onFailure(reason: String?) {
             //   handler.post { current_status_txt.text = "onFailure: reason --> $reason" }
                Log.d(TAG, "onFailure: reason --> $reason")
            }

            override fun onCancel() {
                //handler.post { current_status_txt.text = "onCancel" }
                Log.d(TAG, "onCancel")
            }
        }).build()
    }
}
