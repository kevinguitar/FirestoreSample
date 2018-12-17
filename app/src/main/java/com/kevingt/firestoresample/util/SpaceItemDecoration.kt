package com.kevingt.firestoresample.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private const val MARGIN_VERTICAL = 50
        private const val MARGIN_HORIZONTAL = 50
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = MARGIN_VERTICAL
        outRect.left = MARGIN_HORIZONTAL
        outRect.right = MARGIN_HORIZONTAL
        if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount!! - 1) {
            outRect.bottom = MARGIN_VERTICAL
        }
    }

}