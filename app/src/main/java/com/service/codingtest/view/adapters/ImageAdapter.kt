package com.service.codingtest.view.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.service.codingtest.R
import com.service.codingtest.model.response.DocumentData
import com.service.codingtest.network.MLog
import kotlinx.android.synthetic.main.item_image.view.*

class ImageAdapter: PagingDataAdapter<DocumentData, ImageAdapter.ViewHolder>(ChatDiffCallback) {

    private val TAG = ImageAdapter::class.java.name

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_image, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)!!

        Glide.with(holder.itemView.context).load(data.image_url)
            .placeholder(R.drawable.placeholder)
            .thumbnail(0.1f)
            .into(holder.iv_thumb)

        MLog.d(TAG, data.image_url)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val iv_thumb = itemView.iv_thumb
    }

    companion object {
        private val PAYLOAD_SCORE = Any()
        val ChatDiffCallback = object : DiffUtil.ItemCallback<DocumentData>() {
            override fun areItemsTheSame(oldItem: DocumentData, newItem: DocumentData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DocumentData, newItem: DocumentData): Boolean {
                return oldItem.image_url == newItem.image_url
            }

            override fun getChangePayload(oldItem: DocumentData, newItem: DocumentData): Any? {
                return if (sameExceptScore(oldItem, newItem)) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

        private fun sameExceptScore(oldItem: DocumentData, newItem: DocumentData): Boolean {
            return oldItem.copy(image_url = newItem.image_url) == newItem
        }
    }
}