package com.eugenebrusov.news

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.eugenebrusov.news.api.NewsRetriever
import com.eugenebrusov.news.models.NewsDetailResponse
import com.eugenebrusov.news.models.NewsResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Eugene Brusov on 8/29/17.
 */
class NewsDetailViewModel : ViewModel() {

    private val LogTag = NewsDetailViewModel::class.java.simpleName

    private var newsResult: LiveData<NewsResult>? = null

    fun newsResult(id: String) : LiveData<NewsResult> {
        if (newsResult == null) {
            val data = MutableLiveData<NewsResult>()
            NewsRetriever().getNewsDetail(id, object : Callback<NewsDetailResponse> {
                override fun onResponse(call: Call<NewsDetailResponse>?, response: Response<NewsDetailResponse>?) {
                    response?.isSuccessful.let {
                        data.value = response?.body()?.response?.content
                    }
                }

                override fun onFailure(call: Call<NewsDetailResponse>?, t: Throwable?) {
                    Log.e(LogTag, "onFailure", t)
                }

            })

            newsResult = data
        }
        return newsResult!!
    }
}