package com.eugenebrusov.news.models

/**
 * Created by Eugene Brusov on 8/22/17.
 */
data class NewsResult(val webPublicationDate: String, val fields: NewsFields, val tags: List<NewsTag>)