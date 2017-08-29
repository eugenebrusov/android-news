package com.eugenebrusov.news

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.eugenebrusov.news.api.NewsRetriever
import com.eugenebrusov.news.models.NewsResults
import com.eugenebrusov.news.models.NewsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsListViewModel : ViewModel() {

    private val LogTag = NewsListViewModel::class.java.simpleName

    var newsResults: LiveData<NewsResults>? = null
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

}