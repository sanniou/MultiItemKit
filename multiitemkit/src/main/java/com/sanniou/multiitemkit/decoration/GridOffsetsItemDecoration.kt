package com.sanniou.multiitemkit.decoration

import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * This class can only be used in the RecyclerView which use a GridLayoutManager or
 * StaggeredGridLayoutManager, but it's not always work for the StaggeredGridLayoutManager, because
 * we can't figure out which position should belong to the last column or the last row
 */
class GridOffsetsItemDecoration(@Orientation orientation: Int) :
    ItemDecoration() {
    private val mTypeOffsetsFactories = SparseArray<OffsetsCreator>()

    @IntDef(
        GRID_OFFSETS_HORIZONTAL,
        GRID_OFFSETS_VERTICAL
    )
    @Retention(RetentionPolicy.SOURCE)
    private annotation class Orientation

    @Orientation
    private var mOrientation = 0
    private var mVerticalItemOffsets = 0
    private var mHorizontalItemOffsets = 0
    private var mIsOffsetEdge: Boolean
    private var mIsOffsetLast: Boolean
    fun setOrientation(orientation: Int) {
        mOrientation = orientation
    }

    fun setVerticalItemOffsets(verticalItemOffsets: Int) {
        mVerticalItemOffsets = verticalItemOffsets
    }

    fun setHorizontalItemOffsets(horizontalItemOffsets: Int) {
        mHorizontalItemOffsets = horizontalItemOffsets
    }

    fun setOffsetEdge(isOffsetEdge: Boolean) {
        mIsOffsetEdge = isOffsetEdge
    }

    fun setOffsetLast(isOffsetLast: Boolean) {
        mIsOffsetLast = isOffsetLast
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanCount = getSpanCount(parent)
        val childCount = parent.adapter!!.itemCount
        val adapterPosition = parent.getChildAdapterPosition(view)
        val horizontalOffsets = getHorizontalOffsets(parent, view)
        val verticalOffsets = getVerticalOffsets(parent, view)
        val isFirstRow = isFirstRow(adapterPosition, spanCount, childCount)
        val isLastRow = isLastRow(adapterPosition, spanCount, childCount)
        val isFirstColumn = isFirstColumn(adapterPosition, spanCount, childCount)
        val isLastColumn = isLastColumn(adapterPosition, spanCount, childCount)
        outRect[0, 0, horizontalOffsets] = verticalOffsets
        outRect.bottom =
            if (mOrientation != GRID_OFFSETS_VERTICAL && isLastRow) 0 else verticalOffsets
        outRect.right =
            if (mOrientation != GRID_OFFSETS_HORIZONTAL && isLastColumn) 0 else horizontalOffsets
        if (mIsOffsetEdge) {
            outRect.top = if (isFirstRow) verticalOffsets else 0
            outRect.left = if (isFirstColumn) horizontalOffsets else 0
            outRect.right = horizontalOffsets
            outRect.bottom = verticalOffsets
        }
        if (!mIsOffsetLast) {
            if (mOrientation == GRID_OFFSETS_VERTICAL && isLastRow) {
                outRect.bottom = 0
            } else if (mOrientation == GRID_OFFSETS_HORIZONTAL && isLastColumn) {
                outRect.right = 0
            }
        }
    }

    private fun getHorizontalOffsets(parent: RecyclerView, view: View): Int {
        if (mTypeOffsetsFactories.size() == 0) {
            return mHorizontalItemOffsets
        }
        val adapterPosition = parent.getChildAdapterPosition(view)
        val itemType = parent.adapter!!.getItemViewType(adapterPosition)
        val offsetsCreator = mTypeOffsetsFactories[itemType]
        return offsetsCreator?.createHorizontal(parent, adapterPosition) ?: 0
    }

    private fun getVerticalOffsets(parent: RecyclerView, view: View): Int {
        if (mTypeOffsetsFactories.size() == 0) {
            return mVerticalItemOffsets
        }
        val adapterPosition = parent.getChildAdapterPosition(view)
        val itemType = parent.adapter!!.getItemViewType(adapterPosition)
        val offsetsCreator = mTypeOffsetsFactories[itemType]
        return offsetsCreator?.createVertical(parent, adapterPosition) ?: 0
    }

    private fun isFirstColumn(position: Int, spanCount: Int, childCount: Int): Boolean {
        return if (mOrientation == GRID_OFFSETS_VERTICAL) {
            position % spanCount == 0
        } else {
            var lastColumnCount = childCount % spanCount
            lastColumnCount = if (lastColumnCount == 0) spanCount else lastColumnCount
            position < childCount - lastColumnCount
        }
    }

    private fun isLastColumn(position: Int, spanCount: Int, childCount: Int): Boolean {
        return if (mOrientation == GRID_OFFSETS_VERTICAL) {
            (position + 1) % spanCount == 0
        } else {
            var lastColumnCount = childCount % spanCount
            lastColumnCount = if (lastColumnCount == 0) spanCount else lastColumnCount
            position >= childCount - lastColumnCount
        }
    }

    private fun isFirstRow(position: Int, spanCount: Int, childCount: Int): Boolean {
        return if (mOrientation == GRID_OFFSETS_VERTICAL) {
            position < spanCount
        } else {
            position % spanCount == 0
        }
    }

    private fun isLastRow(position: Int, spanCount: Int, childCount: Int): Boolean {
        return if (mOrientation == GRID_OFFSETS_VERTICAL) {
            var lastColumnCount = childCount % spanCount
            lastColumnCount = if (lastColumnCount == 0) spanCount else lastColumnCount
            position >= childCount - lastColumnCount
        } else {
            (position + 1) % spanCount == 0
        }
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if (layoutManager is GridLayoutManager) {
            layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            layoutManager.spanCount
        } else {
            throw UnsupportedOperationException(
                "the GridDividerItemDecoration can only be used in " +
                    "the RecyclerView which use a GridLayoutManager or StaggeredGridLayoutManager"
            )
        }
    }

    fun registerTypeDrawable(itemType: Int, offsetsCreator: OffsetsCreator) {
        mTypeOffsetsFactories.put(itemType, offsetsCreator)
    }

    interface OffsetsCreator {
        fun createVertical(parent: RecyclerView?, adapterPosition: Int): Int
        fun createHorizontal(parent: RecyclerView?, adapterPosition: Int): Int
    }

    companion object {
        const val GRID_OFFSETS_HORIZONTAL = GridLayoutManager.HORIZONTAL
        const val GRID_OFFSETS_VERTICAL = GridLayoutManager.VERTICAL
    }

    init {
        setOrientation(orientation)
        mIsOffsetLast = true
        mIsOffsetEdge = true
    }
}