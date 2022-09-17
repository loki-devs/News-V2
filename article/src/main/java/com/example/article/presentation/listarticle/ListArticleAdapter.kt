package com.example.article.presentation.listarticle

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.example.article.R
import com.example.article.common.FilterListener
import com.example.article.domain.model.ArticleData
import com.example.core.common.extension.*
import com.example.core.presentation.base.BaseHolder
import com.example.core.presentation.base.BaseLoadingAdapter
import kotlinx.android.synthetic.main.item_article.view.*

class ListArticleAdapter: BaseLoadingAdapter<ArticleData>(), Filterable {

    var originalValues: List<ArticleData> = arrayListOf()
    private var filterListeners: FilterListener? = null

    fun setFilterListeners(filterFinishedListener: FilterListener) {
        filterListeners = filterFinishedListener
    }

    override fun getFilter(): Filter = arrayFilter

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

    private val arrayFilter = object : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val result = FilterResults()
            if (prefix == null || prefix.isEmpty()) {
                result.values = originalValues
                result.count = originalValues.size
            } else {
                val prefixString = prefix.toString().toLowerCase()
                val count = originalValues.size
                val newList: MutableList<ArticleData> = ArrayList(count)

                try {
                    (0 until count)
                        .filter {
                            originalValues[it]?.title?.lowercase()?.contains(prefixString) == true
                        }
                        .mapTo(newList) {
                            originalValues[it]
                        }

                } catch (e: Exception) {
                    Log.e("ERORR", e.message.toString())
                }
                result.values = newList
                result.count = newList.size
            }
            return result
        }

        override fun publishResults(char: CharSequence?, results: FilterResults?) {
            results?.let {
                list = it.values as ArrayList<ArticleData>
                filterListeners?.filteringFinished(it.count)
            }
            notifyDataSetChanged()
        }
    }
}