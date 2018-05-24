package com.eugenebrusov.news.newslist

import android.view.View

/**
 * Interface that allows NewsListFragment to communicate with NewsListActivity
 *
 * Probably, interaction between fragment and activity should be arranged via shared ViewModel,
 * but since it's required to transfer sharedView using LiveData for handle values of View type
 * might bring unexpected problems.
 *
 */
interface OnNewsItemSelectedListener {

    fun onNewsItemSelected(id: String, sharedView: View)
}