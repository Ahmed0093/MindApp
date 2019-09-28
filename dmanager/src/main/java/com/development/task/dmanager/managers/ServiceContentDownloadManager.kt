package com.mindvalley.appcontentloader.utilities

import android.util.Log
import com.development.task.dmanager.callback.ContentServiceObserver
import com.development.task.dmanager.callback.ContentServiceStatusObserver
import com.development.task.dmanager.managers.CachingManager
import com.development.task.dmanager.managers.TaskManager
import com.development.task.dmanager.models.ServiceContentTypeDownload


import com.loopj.android.http.AsyncHttpClient

import java.util.HashMap
import java.util.LinkedList


class ServiceContentDownloadManager private constructor() {
    private val allRequestsByKey = HashMap<String, LinkedList<ServiceContentTypeDownload>>()
    private val allRequestsClient = HashMap<String, AsyncHttpClient>()
    private val contentServiceCachingManager: CachingManager

    val isQueueEmpty: Boolean
        get() = allRequestsByKey.size == 0

    init {
        contentServiceCachingManager = CachingManager.getInstance()
    }

    fun getRequest(serviceContentTypeDownload: ServiceContentTypeDownload) {
        val mKey = serviceContentTypeDownload.keyMD5
        // Check if exist in the cache
        if(contentServiceCachingManager.getMDownloadDataType(mKey) != null) {
            val serviceContentTypeDownloadFromCache = contentServiceCachingManager.getMDownloadDataType(mKey)
                serviceContentTypeDownloadFromCache!!.comeFrom = "Cache"
                // call interface
                val contentServiceObserver = serviceContentTypeDownload.contentServiceObserver
                contentServiceObserver.onStart(serviceContentTypeDownloadFromCache)
                contentServiceObserver.onSuccess(serviceContentTypeDownloadFromCache)
                return
            }

        // This will run if two request come for same url
        if (allRequestsByKey.containsKey(mKey)) {
            serviceContentTypeDownload.comeFrom = "Cache"
            allRequestsByKey[mKey]!!.add(serviceContentTypeDownload)
            return
        }
        // Put it in the allRequestsByKey to manage it after done or cancel
        if (allRequestsByKey.containsKey(mKey)) {
            allRequestsByKey[mKey]!!.add(serviceContentTypeDownload)
        } else {
            val lstMDDataType = LinkedList<ServiceContentTypeDownload>()
            lstMDDataType.add(serviceContentTypeDownload)
            allRequestsByKey[mKey] = lstMDDataType
        }
        // Create new WebCallDataTypeDownload by clone it from the parameter
        val newWebCallDataTypeDownload = serviceContentTypeDownload.getCopyOfMe(object : ContentServiceObserver {
            override fun onStart(mDownloadDataType: ServiceContentTypeDownload) {
                for (m in allRequestsByKey[mDownloadDataType.keyMD5]!!) {
                    m.data = mDownloadDataType.data
                    m.contentServiceObserver.onStart(m)
                }
            }

            override fun onSuccess(mDownloadDataType: ServiceContentTypeDownload) {
                for (m in allRequestsByKey[mDownloadDataType.keyMD5]!!) {
                    m.data = mDownloadDataType.data
                    Log.e("HERRRR", m.comeFrom)
                    m.contentServiceObserver.onSuccess(m)
                }
                allRequestsByKey.remove(mDownloadDataType.keyMD5)
            }

            override fun onFailure(
                mDownloadDataType: ServiceContentTypeDownload,
                statusCode: Int?,
                errorResponse: ByteArray?,
                e: Throwable?
            ) {
                for (m in allRequestsByKey[mDownloadDataType.keyMD5]!!) {
                    m.data =(mDownloadDataType.data)
                    m.contentServiceObserver.onFailure(m, statusCode, errorResponse, e)
                }
                allRequestsByKey.remove(mDownloadDataType.keyMD5)
            }

            override fun onRetry(mDownloadDataType: ServiceContentTypeDownload, retryNo: Int) {
                for (m in allRequestsByKey[mDownloadDataType.keyMD5]!!) {
                    m.data =(mDownloadDataType.data)
                    m.contentServiceObserver.onRetry(m, retryNo)
                }
            }
        })
        // Get from download manager
        val contentServiceSyncTaskManager = TaskManager()
        val client =
            contentServiceSyncTaskManager.get(newWebCallDataTypeDownload, object : ContentServiceStatusObserver {
                override fun setDone(mDownloadDataType: ServiceContentTypeDownload) {
                    // put in the cache when mark as done
                    contentServiceCachingManager.putMDownloadDataType(mDownloadDataType.keyMD5, mDownloadDataType)
                    allRequestsClient.remove(mDownloadDataType.keyMD5)
                }

                override fun onFailure(mDownloadDataType: ServiceContentTypeDownload) {
                    allRequestsClient.remove(mDownloadDataType.keyMD5)
                }

                override fun setCancelled(mDownloadDataType: ServiceContentTypeDownload) {
                    allRequestsClient.remove(mDownloadDataType.url)
                }
            })
        allRequestsClient[mKey] = client
    }
    fun cancelRequest(serviceContentTypeDownload: ServiceContentTypeDownload) {
        if (allRequestsByKey.containsKey(serviceContentTypeDownload.keyMD5)) {
            allRequestsByKey[serviceContentTypeDownload.keyMD5]!!.remove(serviceContentTypeDownload)
            serviceContentTypeDownload.comeFrom = "cancelRequest"
            serviceContentTypeDownload.contentServiceObserver.onFailure(serviceContentTypeDownload, 0, null, null)
        }
    }


    fun clearTheCash() {
        contentServiceCachingManager.clearTheCash()
    }

    companion object {
        private var instance: ServiceContentDownloadManager? = null

        fun getInstance(): ServiceContentDownloadManager {
            if (instance == null) {
                instance = ServiceContentDownloadManager()
            }
            return instance as ServiceContentDownloadManager
        }
    }
}
