package com.example.core.presentation.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.core.R
import com.example.core.common.extension.gone
import com.example.core.common.extension.visible

abstract class BaseLoadingAdapter<T> : BaseAdapter<T, BaseHolder<T>>() {

    var isLoading: Boolean = false
    var pbLoading: ProgressBar? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            LOADING_VIEW ->
                LoadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        getLoadingView(),
                        parent,
                        false
                    ), listener
                )
            else ->
                onCreateMainViewHolder(parent, viewType)
        }

    override fun bindViewHolder(holder: BaseHolder<T>, data: T?) {
        if (holder is LoadingViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    private fun getRealItemCount(): Int {
        return list.size
    }

    abstract fun onCreateMainViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T>

    abstract fun getLoadingView(): Int

    override fun getItemViewType(position: Int) =
        if (position == itemCount - 1) {
            LOADING_VIEW
        } else {
            ITEM_VIEW
        }


    open fun showLoadingFooter() {
        isLoading = true
        notifyItemChanged(itemCount - 1)
    }

    open fun hideLoadingFooter() {
        isLoading = false
        notifyItemChanged(itemCount - 1)
    }

    open fun setPaginationData(dataAfterAdded: List<T?>) {
        val indexBefore = itemCount - 1
        list.addAll(dataAfterAdded)
        notifyItemRangeInserted(indexBefore, dataAfterAdded.size)
    }

    override fun getItem(position: Int): T? {
        return if (position < getRealItemCount()) {
            list[position]
        } else {
            null
        }
    }

    inner class LoadingViewHolder(
        itemView: View,
        listener: (position: Int, T?, viewId: Int) -> Unit = { _, _, _ -> }
    ) : BaseHolder<T>(listener, itemView) {
        init {
            pbLoading = itemView.findViewById(R.id.pbLoading)
        }

        fun bind() {
            if (isLoading) {
                pbLoading?.visible()
            } else {
                pbLoading?.gone()
            }
        }

        override fun onClick(v: View) {}
    }

    companion object {
        val LOADING_VIEW = 1
        val ITEM_VIEW = 2
    }
}