package com.eugenebrusov.news.newslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.switchMap
import com.eugenebrusov.news.SingleLiveEvent
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.NewsItem
import com.eugenebrusov.news.data.source.Repository

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsListViewModel(
        val context: Application,
        val repository: Repository
) : AndroidViewModel(context) {

    private val request = MutableLiveData<String>()
    val newsItems = switchMap(request, {
        repository.loadNews(it)
    })

    val items = MutableLiveData<List<NewsItem>>()
    val dataLoading = MutableLiveData<Boolean>()
    val dataError = MutableLiveData<Boolean>()
    internal val openNewsDetailsEvent = SingleLiveEvent<Int>()

    fun loadNews(request: String) {
        this.request.value = request
    }

    fun start() {
        loadNews()
    }

    fun loadNews() {
        dataError.value = false
        dataLoading.value = true

        repository.getNews(object : DataSource.LoadNewsListCallback {
            override fun onNewsListLoaded(items: List<NewsItem>) {
                this@NewsListViewModel.items.value = items

                dataLoading.value = false
            }

            override fun onDataNotAvailable() {
                this@NewsListViewModel.items.value = null

                dataError.value = true
                dataLoading.value = false
            }
        })
    }

    fun onRefresh() {
        loadNews()
    }

}