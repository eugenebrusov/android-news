package com.eugenebrusov.news.newslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.util.Log
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

    private val LogTag = NewsListViewModel::class.java.simpleName

    val items: ObservableList<NewsResult> = ObservableArrayList()
    internal val openNewsDetailsEvent = SingleLiveEvent<String>()

    fun start() {
        loadNews()
    }

    fun loadNews() {
        repository.getNews(object : DataSource.LoadNewsListCallback {
            override fun onNewsListLoaded(items: List<NewsResult>) {
                with(this@NewsListViewModel.items) {
                    clear()
                    addAll(items)
                }
            }

            override fun onDataNotAvailable() {
                Log.e(LogTag, "onFailure")
            }

        })
    }
}