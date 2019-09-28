package com.development.task.dmanager.managers

import android.util.LruCache
import com.development.task.dmanager.models.ServiceContentTypeDownload

class CachingManager private constructor() {
    private val maxCacheSize: Int
    private val mDownloadDataTypeCache: LruCache<String, ServiceContentTypeDownload>

    init {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        // Use 1/8th of the available memory for this memory cache.
        maxCacheSize = maxMemory / 8 // 4 * 1024 * 1024; // 4MiB
        mDownloadDataTypeCache = LruCache<String, ServiceContentTypeDownload>(maxCacheSize)
    }

    fun getMDownloadDataType(key: String): ServiceContentTypeDownload? {
        return mDownloadDataTypeCache.get(key)
    }

    fun putMDownloadDataType(key: String, serviceContentTypeDownload: ServiceContentTypeDownload): Boolean {
        return mDownloadDataTypeCache.put(key, serviceContentTypeDownload) != null
    }

    fun clearTheCash() {
        mDownloadDataTypeCache.evictAll()
    }

    companion object {
        private var instance: CachingManager? = null

        fun getInstance(): CachingManager {
            if (instance == null) {
                instance = CachingManager()
            }
            return instance as CachingManager
        }
    }
}
