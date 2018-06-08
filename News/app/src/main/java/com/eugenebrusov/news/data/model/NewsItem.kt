package com.eugenebrusov.news.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.eugenebrusov.news.data.source.remote.guardian.json.search.JSONSearchResult
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Immutable model class for a NewsItem.
 *
 * @param id unique id of the news item
 */
@Entity(tableName = "news")
data class NewsItem(
        @PrimaryKey @ColumnInfo(name = "id") val id: String,
        @ColumnInfo(name = "webPublicationDate") val webPublicationDate: Long,
        @ColumnInfo(name = "sectionId") val sectionId: String,
        @ColumnInfo(name = "headline") val headline: String?,
        @ColumnInfo(name = "trailText") val trailText: String?,
        @ColumnInfo(name = "thumbnail") val thumbnail: String?,
        @ColumnInfo(name = "bodyText") val bodyText: String?,
        @ColumnInfo(name = "webTitle") val webTitle: String?,
        @ColumnInfo(name = "bylineImageUrl") val bylineImageUrl: String?
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