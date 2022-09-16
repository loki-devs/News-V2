package com.example.core.presentation.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<S, T : BaseHolder<S>> : RecyclerView.Adapter<T>() {
    var listener: (position: Int, S?, viewId: Int) -> Unit = { _, _, _ -> }
    var imageClickListener: (position: Int, S, view: View) -> Unit = { _, _, _ -> }
    var list: ArrayList<S?> = ArrayList()

    fun setItemClickListener(listener: (position: Int, S?, viewId: Int) -> Unit = { _, _, _ -> }) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        val data = getItem(position)
        holder.bindData(position, data)
        bindViewHolder(holder, data)
    }

    open fun add(data: S) {
        list.add(data)
        notifyItemInserted(itemCount - 1)
    }

    open fun addAll(dataList: ArrayList<S>) {
        val indexBefore = itemCount - 1
        list.addAll(dataList)
        notifyItemRangeInserted(indexBefore, dataList.size)
    }

    open fun resetData(data: List<S>) {
        list = ArrayList(data)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    open fun getItem(position: Int) =
        if (position < list.size) {
            list[position]
        } else {
            null
        }

    override fun getItemCount() = list.size

    protected abstract fun bindViewHolder(holder: T, data: S?)
}