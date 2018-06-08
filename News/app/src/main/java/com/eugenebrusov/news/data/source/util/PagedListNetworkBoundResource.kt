package com.eugenebrusov.news.data.source.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.eugenebrusov.news.data.model.Listing
import com.eugenebrusov.news.data.model.Resource
import com.eugenebrusov.news.data.model.Status
import com.eugenebrusov.news.data.source.remote.util.ApiErrorResponse
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
import com.eugenebrusov.news.data.source.remote.util.ApiSuccessResponse

/**
 * A generic class that can provide a PagedList resource
 * backed by both the sqlite database and the network.
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param ResultType: Type for the Resource data
 * @param RequestType: Type for the API response
*/
abstract class PagedListNetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<Listing<ResultType>>>()
    private val pagedListLiveData: LiveData<PagedList<ResultType>>

    init {

        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val callback = object : PagedList.BoundaryCallback<ResultType>() {
            /**
             * Database returned 0 items. We should query the backend for more items.
             */
            @MainThread
            override fun onZeroItemsLoaded() {
                loadItems()
            }

            /**
             * User reached to the end of the list. App queries network to load items
             * starting from the item at the end of list
             */
            @MainThread
            override fun onItemAtEndLoaded(itemAtEnd: ResultType) {
                loadItems(from = itemAtEnd)
            }
        }

        // create a data source factory from Room
        @Suppress("LeakingThis")
        pagedListLiveData = LivePagedListBuilder(dataSourceFactory(), 20)
                .setBoundaryCallback(callback).build()

        // pagedListLiveData attached as a source to try to query first page from database
        result.addSource(pagedListLiveData) { newData ->
            result.removeSource(pagedListLiveData)

            setValue(Resource.success(Listing(
                    newData,
                    refresh = { loadItems(refresh = true) })))
        }

    }

    @MainThread
    private fun setValue(newValue: Resource<Listing<ResultType>>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun loadItems(from: ResultType? = null, refresh: Boolean = false) {
        if (Status.LOADING != result.value?.status) {
            // pagedListLiveData re-attached as a new source,
            // it will dispatch its latest value quickly
            result.removeSource(pagedListLiveData)
            result.addSource(pagedListLiveData) { newData ->
                result.removeSource(pagedListLiveData)
                val listing = Listing(
                        newData,
                        refresh = { loadItems(refresh = true) })
                setValue(Resource.loading(listing))
            }

            val apiResponse = createCall(from)

            result.addSource(apiResponse) { response ->
                result.removeSource(apiResponse)
                when (response) {
                    is ApiSuccessResponse -> {
                        appExecutors.diskIO().execute {
                            if (refresh) {
                                clearResults()
                            }

                            val results = processResponse(response.body)
                            if (results != null) {
                                saveCallResult(results)
                            }
                            appExecutors.mainThread().execute {
                                result.removeSource(pagedListLiveData)
                                result.addSource(pagedListLiveData) { newData ->
                                    val listing = Listing(
                                            newData,
                                            refresh = { loadItems(refresh = true) })
                                    setValue(Resource.success(listing))
                                }
                            }
                        }
                    }
                    is ApiErrorResponse -> {
                        result.removeSource(pagedListLiveData)
                        result.addSource(pagedListLiveData) { newData ->
                            val listing = Listing(
                                    newData,
                                    retry = { loadItems(from = from) },
                                    refresh = { loadItems(refresh = true) })
                            setValue(Resource.error(response.errorMessage, listing))
                        }
                    }
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<Listing<ResultType>>>

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(items: List<ResultType>)

    // Called to remove set of previously downloaded results and completely refresh current data
    @WorkerThread
    protected abstract fun clearResults()

    // Called to build DataSource.Factory from database for specified ResultType
    @MainThread
    protected abstract fun dataSourceFactory(): DataSource.Factory<Int, ResultType>

    // Called to create API call
    @MainThread
    protected abstract fun createCall(itemAtEnd: ResultType? = null): LiveData<ApiResponse<RequestType>>

    // Called to convert API response RequestType to requested ResultType
    @WorkerThread
    protected abstract fun processResponse(response: RequestType?): List<ResultType>?
}