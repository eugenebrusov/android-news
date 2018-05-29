package com.eugenebrusov.news.data.model

import android.arch.persistence.room.Entity

/**
 * Immutable model class for a NewsSection.
 *
 * @param id unique id of the news item
 */
@Entity(tableName = "sections", primaryKeys = ["id"])
data class NewsSection(
        val id: String,
        val webTitle: String
)