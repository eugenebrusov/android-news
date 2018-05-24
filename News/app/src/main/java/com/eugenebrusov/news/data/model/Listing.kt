package com.eugenebrusov.news.data.model

import android.arch.paging.PagedList

data class Listing<T>(
        // the LiveData of paged lists for the UI to observe
        val pagedList: PagedList<T>?,
        // retries any failed requests
        val retry: () -> Unit = {})