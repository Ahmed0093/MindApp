package com.development.task.dmanager.callback


import com.development.task.dmanager.models.ServiceContentTypeDownload

interface ContentServiceStatusObserver {
    fun setDone(serviceContentTypeDownload: ServiceContentTypeDownload)

    fun setCancelled(serviceContentTypeDownload: ServiceContentTypeDownload)

    fun onFailure(serviceContentTypeDownload: ServiceContentTypeDownload)
}
