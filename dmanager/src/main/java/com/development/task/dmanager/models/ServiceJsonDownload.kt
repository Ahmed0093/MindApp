package com.development.task.dmanager.models

import com.development.task.dmanager.callback.ContentServiceObserver
import com.google.gson.Gson
import java.lang.reflect.Type
import java.nio.charset.Charset

class ServiceJsonDownload(url: String, contentServiceObserver: ContentServiceObserver) :
    ServiceContentTypeDownload(url, ContentDataType.JSON, contentServiceObserver) {

    private val jsonAsString: String
        get() {
            try {
                return data?.let { String(it, Charset.forName("UTF-8")) }.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

    override fun getCopyOfMe(contentServiceObserver: ContentServiceObserver): ServiceContentTypeDownload {
        return ServiceJsonDownload(this.url, contentServiceObserver)
    }

    fun getJson(type: Type): Any {
        val gson = Gson()
        return gson.fromJson(jsonAsString, type)
    }
}
