package com.eugenebrusov.news.newslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.eugenebrusov.news.databinding.ItemNewsListBinding

class NewsListItemViewHolder(
        val binding: ItemNewsListBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup?): NewsListItemViewHolder {
            val binding = ItemNewsListBinding
                    .inflate(LayoutInflater.from(parent?.context), parent, false)
            return NewsListItemViewHolder(binding)
        }
    }
}