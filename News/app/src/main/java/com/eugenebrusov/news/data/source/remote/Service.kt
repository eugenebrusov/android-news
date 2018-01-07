package com.eugenebrusov.news.data.source.remote

import com.eugenebrusov.news.Constants
import com.eugenebrusov.news.data.source.remote.models.NewsListResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * API endpoints defined by the service interface
 */
interface Service {

    @GET("search?api-key=${Constants.API_KEY}&show-tags=contributor&show-fields=all&show-refinements=all")
    fun getNews() : Call<NewsListResponse>
}