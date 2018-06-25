package com.eugenebrusov.news.data.model

import android.arch.persistence.room.Entity
import com.eugenebrusov.news.R
import com.eugenebrusov.news.data.source.remote.guardian.json.sections.JSONSectionsResult
import java.text.ParseException

/**
 * Immutable model class for a NewsSection.
 *
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

                // Temporary show only sections listed below
                return when (id) {
                    "artanddesign" -> NewsSection(id = id, webTitle = webTitle)
                    "books" -> NewsSection(id = id, webTitle = webTitle)
                    "business" -> NewsSection(id = id, webTitle = webTitle)
                    "cities" -> NewsSection(id = id, webTitle = webTitle)
                    "commentisfree" -> NewsSection(id = id, webTitle = webTitle)
                    "culture" -> NewsSection(id = id, webTitle = webTitle)
                    "education" -> NewsSection(id = id, webTitle = webTitle)
                    "environment" -> NewsSection(id = id, webTitle = webTitle)
                    "fashion" -> NewsSection(id = id, webTitle = webTitle)
                    "film" -> NewsSection(id = id, webTitle = webTitle)
                    "football" -> NewsSection(id = id, webTitle = webTitle)
                    "law" -> NewsSection(id = id, webTitle = webTitle)
                    "lifeandstyle" -> NewsSection(id = id, webTitle = webTitle)
                    "media" -> NewsSection(id = id, webTitle = webTitle)
                    "money" -> NewsSection(id = id, webTitle = webTitle)
                    "music" -> NewsSection(id = id, webTitle = webTitle)
                    "politics" -> NewsSection(id = id, webTitle = webTitle)
                    "science" -> NewsSection(id = id, webTitle = webTitle)
                    "society" -> NewsSection(id = id, webTitle = webTitle)
                    "sport" -> NewsSection(id = id, webTitle = webTitle)
                    "stage" -> NewsSection(id = id, webTitle = webTitle)
                    "technology" -> NewsSection(id = id, webTitle = webTitle)
                    "theguardian" -> NewsSection(id = id, webTitle = webTitle)
                    "travel" -> NewsSection(id = id, webTitle = webTitle)
                    "weather" -> NewsSection(id = id, webTitle = webTitle)
                    "world" -> NewsSection(id = id, webTitle = webTitle)
                    else -> null
                }
            } catch (e: ParseException) {
                return null
            }
        }
    }
}