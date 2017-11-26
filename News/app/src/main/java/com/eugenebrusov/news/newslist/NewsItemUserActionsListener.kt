package com.eugenebrusov.news.newslist

import com.eugenebrusov.news.models.NewsResult

/**
 * Created by eugenebrusov on 11/26/17.
 */
interface NewsItemUserActionsListener {
    fun onNewsItemClicked(newsId: String)
}