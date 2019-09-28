package com.development.task.mymapp.mvplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.development.task.dmanager.callback.ContentServiceObserver
import com.development.task.dmanager.models.ServiceContentTypeDownload
import com.development.task.dmanager.models.ServiceImageDownload
import com.development.task.mymapp.R
import com.development.task.mymapp.model.ResultRespone
import com.development.task.mymapp.providers.downloadManagerProvider
import com.mindvalley.appcontentloader.utilities.ServiceContentDownloadManager
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * Created by Ahmed Abdullah on 9/28/2019.
 */
class ListAdapter(
    private val mListener: OnArticleItemClicked,
    private val downloadProvider: ServiceContentDownloadManager = downloadManagerProvider
) : RecyclerView.Adapter<ListAdapter.Companion.ListViewHolder>() {

    companion object {

        class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val titleText: TextView = view.tv_title
            val numberOfLikesText: TextView = view.tv_likes
            val image: ImageView = view.iv_imagethumbnail
        }
    }

    private var resultsList: List<ResultRespone> = ArrayList()

    fun setData(data: Array<ResultRespone>) {
        this.resultsList = data.toList()
        this.notifyDataSetChanged()
    }

    fun clearData() {
        resultsList = emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAdapter.Companion.ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = resultsList[position]
        holder.titleText.text = item.user.name
        holder.numberOfLikesText.text = item.likes.toString()
        val mDataTypeImageCancel = ServiceImageDownload(
            item.urls.thumb,
            object : ContentServiceObserver {
                override fun onFailure(
                    serviceContentTypeDownload: ServiceContentTypeDownload,
                    statusCode: Int?,
                    errorResponse: ByteArray?,
                    e: Throwable?
                ) {
                    holder.image.setImageResource(R.drawable.ic_launcher_background)
                }

                override fun onStart(mDownloadDataType: ServiceContentTypeDownload) {

                }

                override fun onSuccess(mDownloadDataType: ServiceContentTypeDownload) {
                    holder.image.setImageBitmap((mDownloadDataType as ServiceImageDownload).imageBitmap)
                }

                override fun onRetry(mDownloadDataType: ServiceContentTypeDownload, retryNo: Int) {

                }
            })
        downloadProvider.getRequest(mDataTypeImageCancel)
        holder.itemView.setOnClickListener { _ -> mListener.onClick(item) }

    }

    override fun getItemCount(): Int {
        return resultsList.size
    }

    interface OnArticleItemClicked {
        fun onClick(Results: ResultRespone)
    }


}