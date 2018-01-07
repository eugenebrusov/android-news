package com.eugenebrusov.news.newslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableList
import com.eugenebrusov.news.SingleLiveEvent
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.source.NewsItem
import com.eugenebrusov.news.data.source.Repository

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsListViewModel(
        val context: Application,
        val repository: Repository
) : AndroidViewModel(context) {

    val items: ObservableList<NewsItem> = ObservableArrayList<NewsItem>()
    val dataLoading = ObservableBoolean(false)
    val dataError = ObservableBoolean(false)
    internal val openNewsDetailsEvent = SingleLiveEvent<Int>()

    fun start() {
        loadNews()
    }

    fun loadNews() {
        dataError.set(false)
        dataLoading.set(true)

        repository.getNews(object : DataSource.LoadNewsListCallback {
            override fun onNewsListLoaded(items: List<NewsItem>) {
                with(this@NewsListViewModel.items) {
                    clear()
                    addAll(items)
                }

                dataLoading.set(false)
            }

            override fun onDataNotAvailable() {
                items.clear()
                dataError.set(true)
                dataLoading.set(false)
            }
        })
    }

    fun onRefresh() {
        loadNews()
    }

}