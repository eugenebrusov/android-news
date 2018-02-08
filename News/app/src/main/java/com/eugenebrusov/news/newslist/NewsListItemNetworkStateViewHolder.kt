package com.eugenebrusov.news.newslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.eugenebrusov.news.databinding.ItemNewsListNetworkStateBinding

class NewsListItemNetworkStateViewHolder(
        binding: ItemNewsListNetworkStateBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup?): NewsListItemNetworkStateViewHolder {
            val binding = ItemNewsListNetworkStateBinding
                    .inflate(LayoutInflater.from(parent?.context), parent, false)
            return NewsListItemNetworkStateViewHolder(binding)
        }
    }
}