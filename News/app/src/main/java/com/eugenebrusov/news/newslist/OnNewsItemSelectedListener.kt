package com.eugenebrusov.news.newslist

import android.view.View

/**
 * Interface that allows NewsListFragment to communicate with NewsListActivity
 *
 * Probably, interaction between fragment and activity should be arranged via shared ViewModel,
 * but it might bring unexpected problems if LiveData handles values of View class
 * since it's required to transfer sharedView to animate transition between news list and new detail
 *
 */
interface OnNewsItemSelectedListener {

    fun onNewsItemSelected(id: String, sharedView: View)
}