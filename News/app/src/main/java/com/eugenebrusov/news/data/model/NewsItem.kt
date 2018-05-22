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
 * @param entryId          unique id of the news item
 */
@Entity(tableName = "news")
data class NewsItem @JvmOverloads constructor(
        @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "webPublicationDate") var webPublicationDate: Long = -1,
        @ColumnInfo(name = "sectionName") var sectionName: String? = null,
        @ColumnInfo(name = "headline") var headline: String? = null,
        @ColumnInfo(name = "trailText") var trailText: String? = null,
        @ColumnInfo(name = "thumbnail") var thumbnail: String? = null,
        @ColumnInfo(name = "bodyText") var bodyText: String? = null,
        @ColumnInfo(name = "webTitle") var webTitle: String? = null,
        @ColumnInfo(name = "bylineImageUrl") var bylineImageUrl: String? = null
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

                return NewsItem(id = id,
                        webPublicationDate = webPublicationDate,
                        sectionName = result.sectionName?.toLowerCase(),
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