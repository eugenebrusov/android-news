package com.eugenebrusov.news.data.model

import android.arch.paging.PagedList

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
data class Listing<T>(
        // the LiveData of paged lists for the UI to observe
        val pagedList: PagedList<T>?,
        // retries any failed requests
        val retry: () -> Unit = {},
        // refreshes the whole data and fetches it from scratch
        val refresh: () -> Unit = {})