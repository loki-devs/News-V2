package com.example.core.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationListener(
    private val loadMoreAction: () -> Unit
) :
    RecyclerView.OnScrollListener() {
    var isLastPage: Boolean = false
    var itemCount: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == itemCount && !isLastPage && dy > 0) {
            loadMoreAction.invoke()
        }
    }
}