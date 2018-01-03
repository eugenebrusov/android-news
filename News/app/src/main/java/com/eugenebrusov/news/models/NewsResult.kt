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
                      val tags: List<NewsTag>?) : Parcelable {

    constructor() : this(UUID.randomUUID().toString(), null, null, null)

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(NewsFields::class.java.classLoader),
            parcel.createTypedArrayList(NewsTag)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(webPublicationDate)
        parcel.writeParcelable(fields, flags)
        parcel.writeTypedList(tags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsResult> {
        override fun createFromParcel(parcel: Parcel): NewsResult {
            return NewsResult(parcel)
        }

        override fun newArray(size: Int): Array<NewsResult?> {
            return arrayOfNulls(size)
        }
    }
}