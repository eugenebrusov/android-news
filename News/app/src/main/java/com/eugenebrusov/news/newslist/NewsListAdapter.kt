package com.eugenebrusov.news.newslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.eugenebrusov.news.data.source.NewsItem
import com.eugenebrusov.news.databinding.ItemNewsBinding

/**
 * Created by Eugene Brusov on 8/18/17.
 */
class NewsListAdapter(val viewModel: NewsListViewModel)
    : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    private lateinit var items: List<NewsItem>

    fun replaceData(items: List<NewsItem>?) {
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
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.binding?.newsItem = items.get(position)
        holder?.binding?.listener = object : NewsItemUserActionsListener {
            override fun onNewsItemClicked() {
                viewModel.openNewsDetailsEvent.value = position
            }
        }
        holder?.binding?.executePendingBindings()
    }

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)
}