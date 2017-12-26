package com.eugenebrusov.news.newslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.util.Log
import com.eugenebrusov.news.SingleLiveEvent
import com.eugenebrusov.news.api.NewsRetriever
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.source.Repository
import com.eugenebrusov.news.models.NewsListResponse
import com.eugenebrusov.news.models.NewsResult
import com.eugenebrusov.news.models.NewsResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    var newsResults: MutableLiveData<NewsResults>? = null
        get() {
            if (field == null) {
                val data = MutableLiveData<NewsResults>()
                NewsRetriever().getNews(object : Callback<NewsListResponse> {
                    override fun onResponse(call: Call<NewsListResponse>?, response: Response<NewsListResponse>?) {
                        response?.isSuccessful.let {
                            data.value = response?.body()?.response
                        }
                    }

                    override fun onFailure(call: Call<NewsListResponse>?, t: Throwable?) {
                        Log.e(LogTag, "onFailure", t)
                    }
                })
                field = data
            }
            return field
        }
        private set

    fun loadNextPage() {
        NewsRetriever().getNews(object : Callback<NewsListResponse> {
            override fun onResponse(call: Call<NewsListResponse>?, response: Response<NewsListResponse>?) {
                response?.isSuccessful.let {
                    if (response?.body()?.response != null) {
                        val list = mutableListOf<NewsResult>()
                        list?.addAll(newsResults?.value?.results as Iterable<NewsResult>)
                        list?.addAll(response?.body()?.response?.results as Iterable<NewsResult>)

                        val response = response?.body()?.response
                        response?.results = list
                        newsResults?.value = response
                    }
                }
            }

            override fun onFailure(call: Call<NewsListResponse>?, t: Throwable?) {
                Log.e(LogTag, "onFailure", t)
            }
        })
    }

    fun loadNews() {
        repository.getNews(object : DataSource.LoadNewsListCallback {
            override fun onNewsListLoaded(list: List<NewsResult>) {
                with(items) {
                    clear()
                    addAll(list)
                }
            }

            override fun onDataNotAvailable() {
                Log.e(LogTag, "onFailure")
            }

        })
    }
}