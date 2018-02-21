package com.eugenebrusov.news.newslist

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.eugenebrusov.news.R
import com.eugenebrusov.news.data.NetworkState
import com.eugenebrusov.news.data.NewsItem

class NewsListPagedAdapter : PagedListAdapter<NewsItem, RecyclerView.ViewHolder>(ITEM_COMPARATOR) {

    private var networkState: NetworkState? = NetworkState.LOADING

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_news_list -> NewsListItemViewHolder.create(parent)
            R.layout.item_news_list_network_state -> NewsListItemNetworkStateViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_news_list ->
                (holder as NewsListItemViewHolder).apply {
                    binding.newsItem = getItem(position)
                    binding.executePendingBindings()
                }
        }
    }

    override fun getItemViewType(position: Int): Int {
//        return if (hasExtraRow() && position == itemCount - 1) {
//            R.layout.item_news_list_network_state
//        } else {
            return R.layout.item_news_list
//        }
    }

//    override fun getItemCount(): Int {
//        return super.getItemCount() + if (hasExtraRow()) 1 else 0
//    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    companion object {
        val ITEM_COMPARATOR = object : DiffCallback<NewsItem>() {

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
                    oldItem.id == newItem.id

            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
                    oldItem.id == newItem.id

            override fun getChangePayload(oldItem: NewsItem, newItem: NewsItem): Any? = null
        }
    }
}