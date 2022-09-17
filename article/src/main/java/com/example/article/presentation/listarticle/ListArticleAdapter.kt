package com.example.article.presentation.listarticle

import android.view.View
import android.view.ViewGroup
import com.example.article.R
import com.example.article.domain.model.ArticleData
import com.example.core.common.extension.*
import com.example.core.presentation.base.BaseHolder
import com.example.core.presentation.base.BaseLoadingAdapter
import kotlinx.android.synthetic.main.item_article.view.*

class ListArticleAdapter: BaseLoadingAdapter<ArticleData>() {

    override fun bindViewHolder(holder: BaseHolder<ArticleData>, data: ArticleData?) {
        if (holder is ArticleHolder) holder.bind(data)
        else super.bindViewHolder(holder, data)
    }

    override fun onCreateMainViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseHolder<ArticleData> = ArticleHolder(parent.inflate(R.layout.item_article, false))

    inner class ArticleHolder(view: View) : BaseHolder<ArticleData>(listener, view) {
        fun bind(data: ArticleData?) {
            with(itemView) {
                tv_source.text = data?.source?.name
                tv_title.text = data?.title
                if (data?.content.isNullOrBlank()) {
                    tv_content.gone()
                } else {
                    tv_content.visible()
                    tv_content.text = data?.content
                }
                tv_publish.text = data?.publishedAt?.adjustTimePattern()
                iv_article.loadImageUrl(data?.urlToImage)
            }
        }
    }

    override fun getLoadingView(): Int = com.example.core.R.layout.view_load_more

}