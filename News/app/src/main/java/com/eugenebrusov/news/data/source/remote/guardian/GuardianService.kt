package com.eugenebrusov.news.data.source.remote.guardian

import android.arch.lifecycle.LiveData
import com.eugenebrusov.news.data.source.remote.guardian.json.search.JSONSearchBody
import com.eugenebrusov.news.data.source.remote.guardian.json.sections.JSONSectionsBody
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Endpoints defined by The Guardian API
 */
interface GuardianService {

    @GET("search")
    fun search(@Query("section") section: String,
               @Query("to-date") toDate: String?,
               @Query("show-tags") showTags: String = "contributor",
               @Query("show-fields") showFields: String = "trailText,headline,bodyText,thumbnail",
               @Query("page-size") pageSize: Int = 10) : LiveData<ApiResponse<JSONSearchBody>>

    @GET("sections")
    fun sections() : LiveData<ApiResponse<JSONSectionsBody>>
}