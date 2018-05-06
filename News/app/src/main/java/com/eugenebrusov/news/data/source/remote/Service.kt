package com.eugenebrusov.news.data.source.remote

import com.eugenebrusov.news.Constants
import com.eugenebrusov.news.data.source.remote.models.NewsListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API endpoints defined by the service interface
 */
interface Service {

    @GET("search?api-key=${Constants.API_KEY}" +
            "&show-tags=contributor" +
            "&show-fields=trailText,headline,bodyText,thumbnail" +
            "&page-size=10")
    fun getNews(@Query("section") section: String) : Call<NewsListResponse>

    @GET("search?api-key=${Constants.API_KEY}" +
            "&show-tags=contributor" +
            "&show-fields=trailText,headline,bodyText,thumbnail" +
            "&page-size=10")
    fun getNews(@Query("to-date") webPublicationDate: String, @Query("section") section: String) : Call<NewsListResponse>
}