package com.eugenebrusov.news.data.source.remote.guardian.json.search

/**
 * Created by Eugene Brusov on 8/22/17.
 */
data class JSONSearchResult(val id: String?,
                            val sectionId: String?,
                            val webPublicationDate: String?,
                            val fields: JSONSearchResultFields?,
                            val tags: List<JSONSearchResultTag>?)