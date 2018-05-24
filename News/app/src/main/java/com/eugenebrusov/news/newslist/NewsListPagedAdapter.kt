package com.eugenebrusov.news.newslist

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.R
import com.eugenebrusov.news.data.model.Listing
import com.eugenebrusov.news.data.model.NewsItem
import com.eugenebrusov.news.data.model.Resource
import com.eugenebrusov.news.data.model.Status
import com.eugenebrusov.news.databinding.ItemNewsListBinding
import com.eugenebrusov.news.databinding.ItemNewsListErrorStateBinding
import com.eugenebrusov.news.databinding.ItemNewsListLoadingStateBinding

class NewsListPagedAdapter(private val itemClickCallback: ((String, View) -> Unit)?)
    : PagedListAdapter<NewsItem, RecyclerView.ViewHolder>(ITEM_COMPARATOR) {

    var results: Resource<Listing<NewsItem>>? = null
        set(value) {
            if (value == null) {
                return
            }

            super.submitList(value.data?.pagedList)

            val previousResults = field
            val hadExtraRow = hasExtraRow()
            field = value
            val hasExtraRow = hasExtraRow()
            if (hadExtraRow != hasExtraRow) {
                if (hadExtraRow) {
                    notifyItemRemoved(super.getItemCount())
                } else {
                    notifyItemInserted(super.getItemCount())
                }
            } else if (hasExtraRow && previousResults?.status != value.status) {
                notifyItemChanged(itemCount - 1)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_news_list -> NewsListItemViewHolder.create(parent)
            R.layout.item_news_list_loading_state -> NewsListItemLoadingStateViewHolder.create(parent)
            R.layout.item_news_list_error_state -> NewsListItemErrorStateViewHolder.create(parent) {
                results?.data?.retry?.invoke()
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_news_list ->
                (holder as NewsListItemViewHolder).apply {
                    val newsItem = getItem(position)
                    binding.newsItem = newsItem
                    binding.clickListener = View.OnClickListener {
                        itemClickCallback?.invoke(newsItem!!.id, binding.thumbnailImage)
                    }
                    binding.executePendingBindings()
                }
            R.layout.item_news_list_loading_state ->
                (holder as NewsListItemLoadingStateViewHolder).apply {
                    binding.position = position
                    binding.executePendingBindings()
                }
            R.layout.item_news_list_error_state ->
                (holder as NewsListItemErrorStateViewHolder).apply {
                    binding.position = position
                    binding.executePendingBindings()
                }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            if (Status.ERROR == results?.status) {
                R.layout.item_news_list_error_state
            } else {
                R.layout.item_news_list_loading_state
            }
        } else {
            R.layout.item_news_list
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() = (Status.LOADING == results?.status || Status.ERROR == results?.status)

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<NewsItem>() {

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
                    oldItem.id == newItem.id

            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
                    oldItem.id == newItem.id
        }
    }

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

    class NewsListItemLoadingStateViewHolder(
            val binding: ItemNewsListLoadingStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup?): NewsListItemLoadingStateViewHolder {
                val binding = ItemNewsListLoadingStateBinding
                        .inflate(LayoutInflater.from(parent?.context), parent, false)
                return NewsListItemLoadingStateViewHolder(binding)
            }
        }
    }

    class NewsListItemErrorStateViewHolder(
            val binding: ItemNewsListErrorStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup?, retryCallback: () -> Unit): NewsListItemErrorStateViewHolder {
                val binding = ItemNewsListErrorStateBinding
                        .inflate(LayoutInflater.from(parent?.context), parent, false)

                binding.clickListener = View.OnClickListener {
                    retryCallback()
                }
                return NewsListItemErrorStateViewHolder(binding)
            }
        }
    }
}