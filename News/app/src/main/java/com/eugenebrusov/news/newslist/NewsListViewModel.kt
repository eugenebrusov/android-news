package com.eugenebrusov.news.newslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import com.eugenebrusov.news.SingleLiveEvent
import com.eugenebrusov.news.data.source.Repository

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsListViewModel(
        val context: Application,
        val repository: Repository
) : AndroidViewModel(context) {

    private val section = MutableLiveData<String>()

    val resultsResource = switchMap(section, {
        repository.searchNews(it)
    })

    val dataLoading = MutableLiveData<Boolean>()
    val dataError = MutableLiveData<Boolean>()
    internal val openNewsDetailsEvent = SingleLiveEvent<Int>()

    fun loadNews(section: String) {
        this.section.value = section
    }

    fun onRefresh() {
        //loadNews()
    }

}