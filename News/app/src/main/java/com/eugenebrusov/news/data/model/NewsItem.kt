package com.eugenebrusov.news.data.model

import android.arch.persistence.room.Entity
import com.eugenebrusov.news.data.source.remote.guardian.json.search.JSONSearchResult
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Immutable model class for a NewsItem.
 *
 */
@Entity(tableName = "news", primaryKeys = ["id"])
data class NewsItem(
        val id: String,
        val webPublicationDate: Long,
        val sectionId: String,
        val sectionName: String?,
        val headline: String?,
        val trailText: String?,
        val thumbnail: String?,
        val bodyText: String?,
        val webTitle: String?,
        val bylineImageUrl: String?
) {
    companion object {

        fun create(result: JSONSearchResult): NewsItem? {
            try {
                val id = result.id ?: throw ParseException("Invalid news item id", 0)

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                val webPublicationDate = format.parse(result.webPublicationDate).time

                var webTitle: String? = null
                var bylineImageUrl: String? = null
                if (result.tags?.isNotEmpty() == true) {
                    webTitle = result.tags[0].webTitle
                    bylineImageUrl = result.tags[0].bylineImageUrl
                }

                val sectionId = result.sectionId
                        ?: throw ParseException("Invalid news item section id", 0)

                return NewsItem(id = id,
                        webPublicationDate = webPublicationDate,
                        sectionId = sectionId.toLowerCase(),
                        sectionName = result.sectionName,
                        headline = result.fields?.headline,
                        trailText = result.fields?.trailText,
                        thumbnail = result.fields?.thumbnail,
                        bodyText = result.fields?.bodyText,
                        webTitle = webTitle,
                        bylineImageUrl = bylineImageUrl)
            } catch (e: ParseException) {
                return null
            }
        }
    }
}