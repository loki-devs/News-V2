package com.example.core.presentation.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder<T> constructor(
    var listener: (position: Int, T?, viewId: Int) -> Unit = { _, _, _ ->  }, itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    protected var itemPosition: Int = 0
        private set
    protected var item: T? = null
        private set

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        listener(itemPosition, item, view.id)
    }

    open fun bindData(position: Int, data: T?) {
        itemPosition = position
        item = data
    }
}