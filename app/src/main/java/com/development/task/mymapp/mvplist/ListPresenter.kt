package com.development.task.mymapp.mvplist

import android.R
import android.content.Context
import android.os.Handler
import android.util.Log
import com.development.task.dmanager.callback.ContentServiceObserver
import com.development.task.dmanager.models.ServiceContentTypeDownload
import com.development.task.dmanager.models.ServiceJsonDownload
import com.development.task.mymapp.Constants
import com.development.task.mymapp.MainActivity
import com.development.task.mymapp.model.ResultRespone
import com.development.task.mymapp.providers.*
import com.google.gson.Gson
import com.mindvalley.appcontentloader.utilities.ServiceContentDownloadManager
import java.nio.charset.StandardCharsets

/**
 * Created by Ahmed Abdullah on 9/28/2019.
 */
val listPresenter by lazy { ListPresenter() }

class ListPresenter(
    var listView: ListContract.View = listContractView,
    val downloadProvider: ServiceContentDownloadManager = downloadManagerProvider,
    val context: Context = ProvideContext.getContext(),
    val internetHelper :DownloadProvider = internetConnectivityHelper
) : ListContract.Presenter {

    private fun fetchDataFromServer() {
        val mDataTypeJson = ServiceJsonDownload(Constants.API_URL, object : ContentServiceObserver {
            override fun onStart(mDownloadDataType: ServiceContentTypeDownload) {
                Log.d("Respone", "Start")

            }

            override fun onSuccess(mDownloadDataType: ServiceContentTypeDownload) {
                val response = mDownloadDataType.data?.let { String(it, StandardCharsets.UTF_8) }
                val detailsResponses = Gson().fromJson<Array<ResultRespone>>(
                    response,
                    Array<ResultRespone>::class.java
                )
                Log.d("Respone", detailsResponses.toString())

                val handler = Handler()
                handler.postDelayed(
                    {
                        listView.showArticleList(detailsResponses)
                    },
                    context.getResources().getInteger(R.integer.config_mediumAnimTime).toLong()
                )

            }

            override fun onFailure(
                mDownloadDataType: ServiceContentTypeDownload,
                statusCode: Int?,
                errorResponse: ByteArray?,
                e: Throwable?
            ) {
                Log.d("Respone", "failure" + statusCode)

            }

            override fun onRetry(mDownloadDataType: ServiceContentTypeDownload, retryNo: Int) {
                Log.d("Respone", "Retry")
            }
        })
        downloadProvider.getRequest(mDataTypeJson)
    }
    override fun fetchArticles() {
        if(internetHelper.isNetworkAvailable(context)) {
            fetchDataFromServer()
        } else {
            listView.showErrorMessage()
        }
    }
    override fun setView(mainActivity: MainActivity) {
        listView = mainActivity
    }

    override fun onArticleItemCLicked(results: ResultRespone) {
        listView.navigateToDetailsActivity(results)
    }

    override fun onFabIconClicked() {
        downloadProvider.clearTheCash()
        listView.showToastCacheCleared()
    }


}