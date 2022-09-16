package com.example.category.presentation

import android.view.View
import android.view.ViewGroup
import com.example.category.R
import com.example.category.domain.model.CategoryData
import com.example.core.common.extension.inflate
import com.example.core.presentation.base.BaseAdapter
import com.example.core.presentation.base.BaseHolder
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter : BaseAdapter<CategoryData, CategoryAdapter.CategoryHolder>() {

    override fun bindViewHolder(holder: CategoryHolder, data: CategoryData?) {
        data?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder =
        CategoryHolder(parent.inflate(R.layout.item_category, false))

    inner class CategoryHolder(view: View) : BaseHolder<CategoryData>(listener, view) {
        fun bind(data: CategoryData) {
            itemView.tv_category.text = data.name
        }
    }
}