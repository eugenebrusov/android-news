package com.eugenebrusov.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.eugenebrusov.news.models.NewsResults
import kotlinx.android.synthetic.main.item_news.view.*

/**
 * Created by Eugene Brusov on 8/18/17.
 */
class NewsListAdapter:RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    var newsResults: NewsResults? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return newsResults?.results?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_news, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val news = newsResults?.results?.get(position)?.fields

        holder?.titleTextView?.text = news?.headline
        Glide.with(holder?.itemView?.context).load(news?.thumbnail).into(holder?.imageView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.title_text
        val imageView: ImageView = itemView.image_view
    }
}