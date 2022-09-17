package com.example.core.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemSpaceDecoration(var verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalSpaceHeight
        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}