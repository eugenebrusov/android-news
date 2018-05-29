package com.eugenebrusov.news.data.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.util.Log
import com.eugenebrusov.news.data.model.Listing
import com.eugenebrusov.news.data.model.NewsItem
import com.eugenebrusov.news.data.model.NewsSection
import com.eugenebrusov.news.data.model.Resource
import com.eugenebrusov.news.data.source.local.Dao
import com.eugenebrusov.news.data.source.remote.guardian.GuardianService
import com.eugenebrusov.news.data.source.remote.guardian.json.search.JSONSearchBody
import com.eugenebrusov.news.data.source.remote.guardian.json.sections.JSONSectionsBody
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
import com.eugenebrusov.news.data.source.remote.util.ApiSuccessResponse
import com.eugenebrusov.news.data.source.util.AppExecutors
import com.eugenebrusov.news.data.source.util.NetworkBoundResource
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

    fun sections(): LiveData<Resource<List<NewsSection>>> {
        return object : NetworkBoundResource<List<NewsSection>, JSONSectionsBody>(appExecutors) {

            override fun saveCallResult(result: List<NewsSection>) {
                dao.insertNewsSections(result)
            }

            override fun shouldFetch(data: List<NewsSection>?): Boolean {
                return (data == null || data.isEmpty())
            }

            override fun loadFromDb(): LiveData<List<NewsSection>> {
               return dao.sections()
            }

            override fun createCall(): LiveData<ApiResponse<JSONSectionsBody>> {
                return guardianService.sections()
            }

            override fun processResponse(response: ApiSuccessResponse<JSONSectionsBody>): List<NewsSection> {
                return response.body?.response?.results?.mapNotNull { result ->
                    NewsSection.create(result)
                } ?: listOf()
            }
        }.asLiveData()
    }

    /**
     * Returns a Listing contains PagedList<NewsItem> for the given section.
     */
    fun searchNews(section: String): LiveData<Resource<Listing<NewsItem>>> {
        return object : PagedListNetworkBoundResource<NewsItem, JSONSearchBody>(appExecutors) {

            override fun saveCallResult(items: List<NewsItem>) {
                dao.insertNewsItems(items)
            }

            override fun clearResults() {
                dao.deleteNews(section)
            }

            override fun dataSourceFactory(): DataSource.Factory<Int, NewsItem> {
                return dao.searchNews(section)
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

                Log.e("searchNews", "toDate $toDate")
                return guardianService.search(section = section, toDate = toDate)
            }

            override fun processResponse(response: JSONSearchBody?): List<NewsItem>? {
                return response?.response?.results?.mapNotNull {
                    NewsItem.create(it)
                }
            }
        }.asLiveData()
    }

    /**
     * Returns a NewsItem for the given id.
     */
    fun findNewsItem(id: String): LiveData<NewsItem> {
        return dao.findNewsItem(id)
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