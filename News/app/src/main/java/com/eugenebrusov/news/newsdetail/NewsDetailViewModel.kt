package com.eugenebrusov.news.newsdetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.NewsItem
import com.eugenebrusov.news.data.source.Repository

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsDetailViewModel(
        val context: Application,
        val repository: Repository
) : AndroidViewModel(context) {

    val newsItem = MutableLiveData<NewsItem>()

    fun start(newsItemId: String) {
//        repository.getNewsItem(newsItemId, object : DataSource.LoadNewsItemCallback {
//            override fun onNewsItemLoaded(item: NewsItem) {
//                newsItem.value = item
//            }
//
//            override fun onDataNotAvailable() {
//                TODO("not implemented")
//            }
//        })
    }
}