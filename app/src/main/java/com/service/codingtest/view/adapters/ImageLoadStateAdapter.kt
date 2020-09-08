package com.service.codingtest.view.adapters

import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.service.codingtest.R
import com.service.codingtest.databinding.NetworkStateItemBinding
import com.service.codingtest.network.Constant


class ImageLoadStateAdapter(private val adapter: ImageAdapter) :
    LoadStateAdapter<NetworkStateItemViewHolder>() {
    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder(parent) { adapter.retry() }
    }

}

class NetworkStateItemViewHolder(parent: ViewGroup, private val retryCallback: () -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
    ) {
    private val binding = NetworkStateItemBinding.bind(itemView)
    private val progressBar = binding.progressBar
    private val errorMsg = binding.errorMsg
    private val retry = binding.retryButton
        .also {
            it.setOnClickListener { retryCallback() }
        }

    fun bindTo(loadState: LoadState) {

        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        (loadState as? LoadState.Error)?.error?.message.let {
            errorMsg.text = when(it) {
                Constant.IS_EMPTY_LIST -> itemView.context.getString(R.string.empty_search)
                else -> it
            }
        }
    }
}