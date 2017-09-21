package com.eugenebrusov.news.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Eugene Brusov on 8/24/17.
 */
data class NewsTag(val webTitle: String?, val bylineImageUrl: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(webTitle)
        parcel.writeString(bylineImageUrl)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<NewsTag> {
        override fun createFromParcel(parcel: Parcel): NewsTag {
            return NewsTag(parcel)
        }

        override fun newArray(size: Int): Array<NewsTag?> {
            return arrayOfNulls(size)
        }
    }
}