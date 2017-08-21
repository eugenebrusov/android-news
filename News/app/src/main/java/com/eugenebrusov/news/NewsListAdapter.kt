package com.eugenebrusov.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eugenebrusov.news.models.NewsList
import kotlinx.android.synthetic.main.item_news.view.*

/**
 * Created by Eugene Brusov on 8/18/17.
 */
class NewsListAdapter:RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    var newsList : NewsList? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return newsList?.results?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_news, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val news = newsList?.results?.get(position)
        holder?.titleTextView?.text = news?.webTitle
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView

        init {
            titleTextView = itemView.title_text
        }
    }
}