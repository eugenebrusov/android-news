package com.eugenebrusov.news.newslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.eugenebrusov.news.databinding.ItemNewsBinding
import com.eugenebrusov.news.models.NewsResult

/**
 * Created by Eugene Brusov on 8/18/17.
 */
class NewsListAdapter(val viewModel: NewsListViewModel)
    : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    interface OnPageRequestedListener {
        fun onNextPageRequested()
    }

    private lateinit var items: List<NewsResult>

    fun replaceData(items: List<NewsResult>?) {
        if (items != null) {
            this.items = items
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding
                .inflate(LayoutInflater.from(parent?.context), parent, false)
        binding.listener = object : NewsItemUserActionsListener {
            override fun onNewsItemClicked(newsId: String) {
                viewModel.openNewsDetailsEvent.value = newsId
            }
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.binding?.newsResult = items.get(position)
        holder?.binding?.executePendingBindings()
    }

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)
}