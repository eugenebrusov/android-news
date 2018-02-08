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
        @PrimaryKey @ColumnInfo(name = "entryId") var entryId: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "webPublicationDate") var webPublicationDate: String = "",
        @ColumnInfo(name = "headline") var headline: String = "",
        @ColumnInfo(name = "trailText") var trailText: String = "",
        @ColumnInfo(name = "thumbnail") var thumbnail: String = "",
        @ColumnInfo(name = "bodyText") var bodyText: String = "",
        @ColumnInfo(name = "webTitle") var webTitle: String = "",
        @ColumnInfo(name = "bylineImageUrl") var bylineImageUrl: String = ""
)