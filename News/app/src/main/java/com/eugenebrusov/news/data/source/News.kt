package com.eugenebrusov.news.data.source

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Immutable model class for a News.
 *
 * @param entryId          unique id of the news item
 */
@Entity(tableName = "news")
data class News @JvmOverloads constructor(
        @PrimaryKey @ColumnInfo(name = "entryId") var entryId: String = UUID.randomUUID().toString()
) {

}