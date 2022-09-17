package com.example.newsource.presentation

import android.view.View
import android.view.ViewGroup
import com.example.core.common.extension.inflate
import com.example.core.presentation.base.BaseHolder
import com.example.core.presentation.base.BaseLoadingAdapter
import com.example.newsource.R
import com.example.newsource.domain.model.NewsSourceData
import kotlinx.android.synthetic.main.item_news_source.view.*

class NewsSourceAdapter: BaseLoadingAdapter<NewsSourceData>() {

    override fun bindViewHolder(holder: BaseHolder<NewsSourceData>, data: NewsSourceData?) {
        if (holder is NewsSourceHolder) holder.bind(data)
        else super.bindViewHolder(holder, data)
    }

    override fun onCreateMainViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseHolder<NewsSourceData> = NewsSourceHolder(parent.inflate(R.layout.item_news_source, false))


    inner class NewsSourceHolder(view: View) : BaseHolder<NewsSourceData>(listener, view) {
        fun bind(data: NewsSourceData?) {
            itemView.tv_source.text = data?.name
        }
    }

    override fun getLoadingView(): Int = com.example.core.R.layout.view_load_more
}