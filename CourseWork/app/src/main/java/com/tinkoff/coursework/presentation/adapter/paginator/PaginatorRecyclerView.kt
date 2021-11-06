package com.tinkoff.coursework.presentation.adapter.paginator

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginatorRecyclerView constructor(
    private val loadMoreItems: () -> Unit,
    private val offset: Int = 0
) : RecyclerView.OnScrollListener() {

    var isLoading: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (isLoading) return
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        if (layoutManager.findLastVisibleItemPosition() - offset < 0) {
            loadMoreItems()
        }
    }
}