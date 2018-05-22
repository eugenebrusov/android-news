package com.eugenebrusov.news.data.source.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.eugenebrusov.news.data.model.NewsItem
import com.eugenebrusov.news.data.model.Resource
import com.eugenebrusov.news.data.model.Status
import com.eugenebrusov.news.data.source.remote.util.ApiErrorResponse
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
import com.eugenebrusov.news.data.source.remote.util.ApiSuccessResponse

abstract class PagedListNetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<PagedList<NewsItem>>>()
    private val pagedListLiveData: LiveData<PagedList<NewsItem>>

    init {

        val callback = object : PagedList.BoundaryCallback<NewsItem>() {
            @MainThread
            override fun onZeroItemsLoaded() {
                processBoundaryCallback()
            }

            @MainThread
            override fun onItemAtEndLoaded(itemAtEnd: NewsItem) {
                processBoundaryCallback(itemAtEnd)
            }
        }

        // create a data source factory from Room
        @Suppress("LeakingThis")
        pagedListLiveData = LivePagedListBuilder(dataSourceFactory(), 20)
                .setBoundaryCallback(callback).build()

        result.addSource(pagedListLiveData) { newData ->
            result.removeSource(pagedListLiveData)
            setValue(Resource.success(newData))
        }

    }

    @MainThread
    private fun setValue(newValue: Resource<PagedList<NewsItem>>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun processBoundaryCallback(itemAtEnd: NewsItem? = null) {
        if (Status.LOADING != result.value?.status) {
            result.removeSource(pagedListLiveData)
            result.addSource(pagedListLiveData) { newData ->
                result.removeSource(pagedListLiveData)
                setValue(Resource.loading(newData))
            }

            val apiResponse
                    = createCall(itemAtEnd?.webPublicationDate?.minus(1))

            result.addSource(apiResponse) { response ->
                result.removeSource(apiResponse)
                when (response) {
                    is ApiSuccessResponse -> {
                        appExecutors.diskIO().execute {
                            val results = processResponse(response.body)
                            if (results != null) {
                                saveCallResult(results)
                            }
                            appExecutors.mainThread().execute {
                                result.removeSource(pagedListLiveData)
                                result.addSource(pagedListLiveData) { newData ->
                                    setValue(Resource.success(newData))
                                }
                            }
                        }
                    }
                    is ApiErrorResponse -> {
                        result.removeSource(pagedListLiveData)
                        result.addSource(pagedListLiveData) { newData ->
                            setValue(Resource.error(response.errorMessage, newData))
                        }
                    }
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<PagedList<NewsItem>>>

    protected open fun onFetchFailed() {}

    @WorkerThread
    protected abstract fun processResponse(response: RequestType?): List<NewsItem>?

    @WorkerThread
    protected abstract fun saveCallResult(items: List<NewsItem>)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun dataSourceFactory(): DataSource.Factory<Int, NewsItem>

    @MainThread
    protected abstract fun createCall(timestamp: Long? = null): LiveData<ApiResponse<RequestType>>
}