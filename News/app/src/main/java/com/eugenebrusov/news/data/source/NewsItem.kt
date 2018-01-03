package com.eugenebrusov.news.data.source

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
        @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {

}