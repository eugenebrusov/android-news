package com.eugenebrusov.news.data.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.eugenebrusov.news.data.source.model.Resource
import com.eugenebrusov.news.data.source.remote.util.ApiEmptyResponse
import com.eugenebrusov.news.data.source.remote.util.ApiErrorResponse
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
import com.eugenebrusov.news.data.source.remote.util.ApiSuccessResponse

abstract class PagedListNetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    setValue(Resource.success(processResponse(response.body)))
                }
                is ApiEmptyResponse -> {
//                    appExecutors.mainThread().execute {
//                        // reload from disk whatever we had
//                        result.addSource(loadFromDb()) { newData ->
//                            setValue(Resource.success(newData))
//                        }
//                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
//                    result.addSource(dbSource) { newData ->
//                        setValue(Resource.error(response.errorMessage, newData))
//                    }
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected open fun onFetchFailed() {}

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @WorkerThread
    protected abstract fun saveCallResult(item: ResultType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

//    @MainThread
//    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}