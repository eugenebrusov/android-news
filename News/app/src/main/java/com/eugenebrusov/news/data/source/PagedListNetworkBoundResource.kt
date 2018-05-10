package com.eugenebrusov.news.data.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import android.util.Log
import com.eugenebrusov.news.data.NewsItem
import com.eugenebrusov.news.data.source.model.Resource
import com.eugenebrusov.news.data.source.remote.util.ApiEmptyResponse
import com.eugenebrusov.news.data.source.remote.util.ApiErrorResponse
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
import com.eugenebrusov.news.data.source.remote.util.ApiSuccessResponse

abstract class PagedListNetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<PagedList<NewsItem>>>()
    private val pagedListLiveData: LiveData<PagedList<NewsItem>>

    private var loading = false

    init {

        val callback = object : PagedList.BoundaryCallback<NewsItem>() {
            @MainThread
            override fun onZeroItemsLoaded() {
                Log.e("boundaryCallback", "onZeroItemsLoaded() #110")
                if (!loading) {
                    loading = true
                    Log.e("boundaryCallback", "onZeroItemsLoaded() #120")
                    val apiResponse = createInitialCall()

//                    result.addSource(getPagedListLiveData()) { newData ->
//                        setValue(Resource.loading(newData))
//                    }

                    result.addSource(apiResponse) { response ->
                        result.removeSource(apiResponse)
                        //result.removeSource(getPagedListLiveData())
                        Log.e("boundaryCallback", "response #130")
                        when (response) {
                            is ApiSuccessResponse -> {
                                Log.e("boundaryCallback", "response #140 " + processResponse(response.body))
                                saveCallResult(processResponse(response.body))
                                result.addSource(getPagedListLiveData()) { newData ->
                                    setValue(Resource.success(newData))
                                }
                            }
                            is ApiEmptyResponse -> {
                                Log.e("boundaryCallback", "response #150")
                            }
                            is ApiErrorResponse -> {
                                Log.e("boundaryCallback", "response #160")
                                result.addSource(getPagedListLiveData()) { newData ->
                                    setValue(Resource.error(response.errorMessage, newData))
                                }
                            }

                        }
                        loading = false
                    }
                }
            }

            @MainThread
            override fun onItemAtEndLoaded(itemAtEnd: NewsItem) {
                Log.e("boundaryCallback", "onItemAtEndLoaded() " + result.value?.status)
                if (!loading) {
                    loading = true
                    val apiResponse
                            = createNextCall(itemAtEnd.webPublicationDate)

//                    result.addSource(getPagedListLiveData()) { newData ->
//                        setValue(Resource.loading(newData))
//                    }

                    result.addSource(apiResponse) { response ->
                        result.removeSource(apiResponse)
                        result.removeSource(getPagedListLiveData())
                        when (response) {
                            is ApiSuccessResponse -> {
                                saveCallResult(processResponse(response.body))
                                result.addSource(getPagedListLiveData()) { newData ->
                                    setValue(Resource.success(newData))
                                }
                            }
                            is ApiEmptyResponse -> {

                            }
                            is ApiErrorResponse -> {
                                result.addSource(getPagedListLiveData()) { newData ->
                                    setValue(Resource.error(response.errorMessage, newData))
                                }
                            }
                        }
                        loading = false
                    }
                }
            }
        }

        // create a data source factory from Room
        @Suppress("LeakingThis")
        pagedListLiveData = LivePagedListBuilder(dataSourceFactory(), 20)
                .setBoundaryCallback(callback).build()

        Log.e("init", "addSource()")
        result.addSource(getPagedListLiveData()) { newData ->
            Log.e("init", "removeSource()")
            result.removeSource(getPagedListLiveData())
            setValue(Resource.loading(newData))
        }

    }

    @MainThread
    private fun setValue(newValue: Resource<PagedList<NewsItem>>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun getPagedListLiveData(): LiveData<PagedList<NewsItem>> {
        return pagedListLiveData
    }

//    private fun fetchFromNetwork() {
//        val apiResponse = createInitialCall()
//        result.addSource(apiResponse) { response ->
//            result.removeSource(apiResponse)
//            when (response) {
//                is ApiSuccessResponse -> {
//                    setValue(Resource.success(processResponse(response.body)))
//                }
//                is ApiEmptyResponse -> {
////                    appExecutors.mainThread().execute {
////                        // reload from disk whatever we had
////                        result.addSource(loadFromDb()) { newData ->
////                            setValue(Resource.success(newData))
////                        }
////                    }
//                }
//                is ApiErrorResponse -> {
//                    onFetchFailed()
//                    setValue(Resource.error(response.errorMessage, null))
////                    result.addSource(dbSource) { newData ->
////                        setValue(Resource.error(response.errorMessage, newData))
////                    }
//                }
//            }
//        }
//    }

    fun asLiveData() = result as LiveData<Resource<PagedList<NewsItem>>>

    protected open fun onFetchFailed() {}

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): List<NewsItem>

    @WorkerThread
    protected abstract fun saveCallResult(items: List<NewsItem>)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun dataSourceFactory(): DataSource.Factory<Int, NewsItem>

    @MainThread
    protected abstract fun createInitialCall(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected abstract fun createNextCall(timestamp: Long): LiveData<ApiResponse<RequestType>>
}