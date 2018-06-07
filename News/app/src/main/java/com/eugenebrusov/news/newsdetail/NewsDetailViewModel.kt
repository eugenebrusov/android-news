package com.eugenebrusov.news.newsdetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.eugenebrusov.news.data.model.NewsItem
import com.eugenebrusov.news.data.source.Repository

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsDetailViewModel(
        val context: Application,
        val repository: Repository
) : AndroidViewModel(context) {

    private val id = MutableLiveData<String>()
    val newsItem = Transformations.switchMap(id) { id ->
        repository.findNewsItem(id)
    }

    fun findNewsItem(id: String) {
        this.id.value = id
    }
}