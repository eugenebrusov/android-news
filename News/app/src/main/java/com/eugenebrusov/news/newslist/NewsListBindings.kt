package com.eugenebrusov.news.newslist

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.eugenebrusov.news.models.NewsResult
import com.eugenebrusov.news.models.NewsResults

/**
 * Created by eugene on 11/20/17.
 */
object NewsListBindings {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(recyclerView: RecyclerView, items: List<NewsResult>?) {
        with(recyclerView.adapter as NewsListAdapter) {
            replaceData(items)
        }
    }

}