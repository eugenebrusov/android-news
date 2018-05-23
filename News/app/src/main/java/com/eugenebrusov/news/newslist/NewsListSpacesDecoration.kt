package com.eugenebrusov.news.newslist

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.eugenebrusov.news.R

class NewsListSpacesDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        val margin = parent?.context?.resources?.getDimension(R.dimen.news_item_margin)?.toInt()
        if (parent?.getChildLayoutPosition(view) == 0) {
            outRect?.top = margin
        }

        outRect?.left = margin
        outRect?.right = margin
        outRect?.bottom = margin
    }
}