package com.eugenebrusov.news.data.model

import android.arch.persistence.room.Entity
import com.eugenebrusov.news.data.source.remote.guardian.json.sections.JSONSectionsResult
import java.text.ParseException

/**
 * Immutable model class for a NewsSection.
 *
 * @param id unique id of the news item
 */
@Entity(tableName = "sections", primaryKeys = ["id"])
data class NewsSection(
        val id: String,
        val webTitle: String
) {

    companion object {

        fun create(result: JSONSectionsResult): NewsSection? {
            try {
                val id = result.id ?: throw ParseException("Invalid section id", 0)
                val webTitle = result.webTitle ?: throw ParseException("Invalid section title", 0)

                // Temporary skip long section names
                if (webTitle.length > 25) {
                    return null
                }

                return NewsSection(id = id, webTitle = webTitle)
            } catch (e: ParseException) {
                return null
            }
        }
    }
}