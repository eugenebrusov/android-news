package com.eugenebrusov.news.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.UUID

/**
 * Immutable model class for a NewsItem.
 *
 * @param entryId          unique id of the news item
 */
@Entity(tableName = "news")
data class NewsItem @JvmOverloads constructor(
        @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "webPublicationDate") var webPublicationDate: Long = -1,
        @ColumnInfo(name = "headline") var headline: String? = null,
        @ColumnInfo(name = "trailText") var trailText: String? = null,
        @ColumnInfo(name = "thumbnail") var thumbnail: String? = null,
        @ColumnInfo(name = "bodyText") var bodyText: String? = null,
        @ColumnInfo(name = "webTitle") var webTitle: String? = null,
        @ColumnInfo(name = "bylineImageUrl") var bylineImageUrl: String? = null
)