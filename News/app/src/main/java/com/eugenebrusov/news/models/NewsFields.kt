package com.eugenebrusov.news.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Eugene Brusov on 8/22/17.
 */
data class NewsFields(val headline: String?,
                      val trailText: String?,
                      val thumbnail: String?,
                      val bodyText: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(headline)
        parcel.writeString(trailText)
        parcel.writeString(thumbnail)
        parcel.writeString(bodyText)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<NewsFields> {
        override fun createFromParcel(parcel: Parcel): NewsFields {
            return NewsFields(parcel)
        }

        override fun newArray(size: Int): Array<NewsFields?> {
            return arrayOfNulls(size)
        }
    }
}