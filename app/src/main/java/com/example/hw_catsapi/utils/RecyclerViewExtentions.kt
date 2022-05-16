package com.example.hw_catsapi.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


fun RecyclerView.addPagingScrollFlow(
    layoutManager: LinearLayoutManager,
    itemsToLoad: Int,
    lastItem: Int
) = callbackFlow {
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

            if (dy != 0 && totalItemCount <= (lastVisibleItem + itemsToLoad) && lastItem > totalItemCount) {
                trySend(Unit)
            }
        }
    }

    addOnScrollListener(listener)
    awaitClose {
        removeOnScrollListener(listener)
    }
}

fun RecyclerView.addBottomSpaceDecorationRes(@DimenRes bottomSpaceRes: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val itemCount = parent.adapter?.itemCount ?: return
            val position = parent.getChildAdapterPosition(view)
            if (position != itemCount - 1) {
                outRect.bottom = bottomSpaceRes
            }
        }
    })
}

