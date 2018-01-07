package com.eugenebrusov.news.util

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eugenebrusov.news.data.source.NewsItem
import com.eugenebrusov.news.newslist.NewsListAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Eugene Brusov on 1/7/18.
 */
object Bindings {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(recyclerView: RecyclerView, items: List<NewsItem>?) {
        with(recyclerView.adapter as NewsListAdapter) {
            replaceData(items)
        }
    }

    @BindingAdapter("app:thumbnail")
    @JvmStatic fun setThumbnail(imageView: ImageView, thumbnail: String) {
        Glide.with(imageView.context).load(thumbnail).into(imageView)
    }

    @BindingAdapter("app:byline")
    @JvmStatic fun setByline(imageView: ImageView, bylineImageUrl: String) {
        Glide.with(imageView.context)
                .load(bylineImageUrl)
                .apply(RequestOptions().circleCrop())
                .into(imageView)
    }

    @BindingAdapter("app:webPublicationDate")
    @JvmStatic fun setWebPublicationDate(textView: TextView, webPublicationDate: String) {
        val date: Date? =
                try {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                            .parse(webPublicationDate)
                } catch (e: ParseException) { null }
        val formattedDate: String? =
                try {
                    SimpleDateFormat("MMM d, yyyy", Locale.US).format(date)
                } catch (e: ParseException) { null }
        textView.text = formattedDate
    }

    @BindingAdapter("app:body")
    @JvmStatic fun setBody(textView: TextView, bodyText: String) {
        var bodyText = bodyText.replace(". ", ".\n\n")
        textView.text = bodyText
    }
}