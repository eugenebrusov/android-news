package com.eugenebrusov.news.newslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableList
import com.eugenebrusov.news.SingleLiveEvent
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.source.Repository
import com.eugenebrusov.news.models.NewsResult

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsListViewModel(
        val context: Application,
        val repository: Repository
) : AndroidViewModel(context) {

    val items: ObservableList<NewsResult> = ObservableArrayList<NewsResult>()
    val dataLoading = ObservableBoolean(false)
    val dataError = ObservableBoolean(false)
    internal val openNewsDetailsEvent = SingleLiveEvent<String>()

    fun start() {
        loadNews()
    }

    fun loadNews() {
        dataError.set(false)
        dataLoading.set(true)

        repository.getNews(object : DataSource.LoadNewsListCallback {
            override fun onNewsListLoaded(items: List<NewsResult>) {
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