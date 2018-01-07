package com.eugenebrusov.news.data.source.remote.models

/**
 * Created by Eugene Brusov on 8/22/17.
 */
data class NewsResult(val id: String?,
                      val webPublicationDate: String?,
                      val fields: NewsFields?,
                      val tags: List<NewsTag>?)