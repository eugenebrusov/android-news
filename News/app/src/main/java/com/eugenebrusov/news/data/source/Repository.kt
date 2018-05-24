package com.eugenebrusov.news.data.source

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.paging.PagedList
import com.eugenebrusov.news.data.model.Listing
import com.eugenebrusov.news.data.model.NewsItem
import com.eugenebrusov.news.data.model.Resource
import com.eugenebrusov.news.data.source.local.Dao
import com.eugenebrusov.news.data.source.remote.guardian.GuardianService
import com.eugenebrusov.news.data.source.remote.guardian.json.search.JSONSearchBody
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
import com.eugenebrusov.news.data.source.util.AppExecutors
import com.eugenebrusov.news.data.source.util.PagedListNetworkBoundResource
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Concrete implementation to load news from the data sources.
 */
class Repository(
        private val appExecutors: AppExecutors,
        private val dao: Dao,
        private val guardianService: GuardianService
) {

    fun searchNews(request: String): LiveData<Resource<Listing<NewsItem>>> {

        return object : PagedListNetworkBoundResource<NewsItem, JSONSearchBody>(appExecutors) {

            override fun saveCallResult(items: List<NewsItem>) {
                dao.insertNewsItems(items)
            }

            override fun dataSourceFactory(): DataSource.Factory<Int, NewsItem> {
                return dao.searchNews(request)
            }

            override fun createCall(itemAtEnd: NewsItem?): LiveData<ApiResponse<JSONSearchBody>> {
                val toDate: String? =
                        if (itemAtEnd != null) {
                            try {
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                                        .format(Date(itemAtEnd.webPublicationDate.minus(1)))
                            } catch (e: ParseException) {
                                null
                            }
                        } else {
                            null
                        }

                return guardianService.search(section = request, toDate = toDate)
            }

            override fun processResponse(response: JSONSearchBody?): List<NewsItem>? {
                return response?.response?.results?.mapNotNull {
                    NewsItem.create(it)
                }
            }
        }.asLiveData()
    }

    companion object {

        private var INSTANCE: Repository? = null

        @JvmStatic fun getInstance(appExecutors: AppExecutors,
                                   dao: Dao,
                                   guardianService: GuardianService) =
                INSTANCE ?: synchronized(Repository::class.java) {
                    INSTANCE ?: Repository(appExecutors, dao, guardianService)
                            .also { INSTANCE = it }
                }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }

}