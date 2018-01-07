package com.eugenebrusov.news.models

import android.os.Parcel
import android.os.Parcelable
import java.util.UUID

/**
 * Created by Eugene Brusov on 8/22/17.
 */
data class NewsResult(val id: String?,
                      val webPublicationDate: String?,
                      val fields: NewsFields?,
                      val tags: List<NewsTag>?)