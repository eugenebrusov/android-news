package com.eugenebrusov.news.newslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import com.eugenebrusov.news.data.model.Status
import com.eugenebrusov.news.data.source.Repository

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsListViewModel(
        val context: Application,
        val repository: Repository
) : AndroidViewModel(context) {

    val sectionsResource = repository.sections()

    val sectionsVisible = map(sectionsResource) { resource ->
        when (resource.status) {
            Status.SUCCESS -> resource?.data?.isEmpty() == false
            Status.LOADING -> false
            Status.ERROR -> false
        }
    }

    private val section = MutableLiveData<String>()

    val resultsResource = switchMap(section) { section ->
        repository.searchNews(section)
    }

    val nestedScrollingEnabled = map(resultsResource) { results ->
        val count = results.data?.pagedList?.size ?: 0
        !((Status.LOADING == results.status || Status.ERROR == results.status)  && count == 0)
    }

    val refreshEnabled = map(resultsResource) { results ->
        val count = results.data?.pagedList?.size ?: 0
        !((Status.LOADING == results.status || Status.ERROR == results.status) && count == 0)
    }

    val refreshing = map(resultsResource) { results ->
        val count = results.data?.pagedList?.size ?: 0
        Status.LOADING == results.status && count == 0
    }

    fun loadNews(section: String) {
        this.section.value = section
    }

    fun refresh() {
        resultsResource.value?.data?.refresh?.invoke()
    }
}