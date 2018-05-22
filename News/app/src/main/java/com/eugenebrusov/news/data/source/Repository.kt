package com.eugenebrusov.news.data.source

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
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

    fun searchNews(section: String): LiveData<Resource<PagedList<NewsItem>>> {
        return object : PagedListNetworkBoundResource<List<NewsItem>, JSONSearchBody>(appExecutors) {

            override fun dataSourceFactory(): android.arch.paging.DataSource.Factory<Int, NewsItem> {
                return dao.searchNews(section)
            }

            override fun processResponse(response: JSONSearchBody?): List<NewsItem>? {
                return response?.response?.results?.mapNotNull {
                    NewsItem.create(it)
                }
            }

            override fun saveCallResult(items: List<NewsItem>) {
                dao.insertNewsItems(items)
            }

            override fun createCall(timestamp: Long?): LiveData<ApiResponse<JSONSearchBody>> {
                val toDate: String? =
                        if (timestamp != null) {
                            try {
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                                        .format(Date(timestamp))
                            } catch (e: ParseException) {
                                null
                            }
                        } else {
                            null
                        }

                return guardianService.search(section = section, toDate = toDate)
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