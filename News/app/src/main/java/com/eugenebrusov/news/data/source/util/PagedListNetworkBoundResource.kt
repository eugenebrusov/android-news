package com.eugenebrusov.news.data.source.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import android.util.Log
import com.eugenebrusov.news.data.model.Resource
import com.eugenebrusov.news.data.model.Status
import com.eugenebrusov.news.data.source.remote.util.ApiErrorResponse
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
import com.eugenebrusov.news.data.source.remote.util.ApiSuccessResponse

abstract class PagedListNetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors,
                        private val retry: Boolean) {

    private val result = MediatorLiveData<Resource<PagedList<ResultType>>>()
    private val pagedListLiveData: LiveData<PagedList<ResultType>>

    init {

        val callback = object : PagedList.BoundaryCallback<ResultType>() {
            @MainThread
            override fun onZeroItemsLoaded() {
                Log.e("BoundResource", "onZeroItemsLoaded")
                processBoundaryCallback()
            }

            @MainThread
            override fun onItemAtEndLoaded(itemAtEnd: ResultType) {
                Log.e("BoundResource", "onItemAtEndLoaded")
                processBoundaryCallback(itemAtEnd)
            }
        }

        // create a data source factory from Room

        @Suppress("LeakingThis")
        pagedListLiveData = LivePagedListBuilder(dataSourceFactory(), 20)
                .setBoundaryCallback(callback).build()

        Log.e("BoundResource", "init #110")
        result.addSource(pagedListLiveData) { newData ->
            result.removeSource(pagedListLiveData)
            Log.e("BoundResource", "init #120 ${newData?.size}")

            if (retry) {
                Log.e("BoundResource", "init #130")
                if (newData != null && newData.size > 0) {
                    val itemAtEnd = newData.get(newData.size - 1)
                    Log.e("BoundResource", "init #125 $itemAtEnd")
                    processBoundaryCallback(itemAtEnd)
                }
            } else {
                setValue(Resource.success(newData))
            }
        }

    }

    @MainThread
    private fun setValue(newValue: Resource<PagedList<ResultType>>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun processBoundaryCallback(itemAtEnd: ResultType? = null) {
        if (Status.LOADING != result.value?.status) {
            result.removeSource(pagedListLiveData)
            result.addSource(pagedListLiveData) { newData ->
                result.removeSource(pagedListLiveData)
                setValue(Resource.loading(newData))
            }

            val apiResponse
                    = createCall(itemAtEnd)

            result.addSource(apiResponse) { response ->
                result.removeSource(apiResponse)
                result.removeSource(pagedListLiveData)
                when (response) {
                    is ApiSuccessResponse -> {
                        appExecutors.diskIO().execute {
                            val results = processResponse(response.body)
                            if (results != null) {
                                saveCallResult(results)
                            }
                            appExecutors.mainThread().execute {
                                result.addSource(pagedListLiveData) { newData ->
                                    setValue(Resource.success(newData))
                                }
                            }
                        }
                    }
                    is ApiErrorResponse -> {
                        result.addSource(pagedListLiveData) { newData ->
                            setValue(Resource.error(response.errorMessage, newData))
                        }
                    }
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<PagedList<ResultType>>>

    @WorkerThread
    protected abstract fun saveCallResult(items: List<ResultType>)

    @MainThread
    protected abstract fun dataSourceFactory(): DataSource.Factory<Int, ResultType>

    @MainThread
    protected abstract fun createCall(itemAtEnd: ResultType? = null): LiveData<ApiResponse<RequestType>>

    @WorkerThread
    protected abstract fun processResponse(response: RequestType?): List<ResultType>?
}