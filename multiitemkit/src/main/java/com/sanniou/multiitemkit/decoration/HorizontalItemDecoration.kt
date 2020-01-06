package com.sanniou.multiitemkit.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class HorizontalItemDecoration(
    private val mDividerViewTypeMap: SparseArray<Drawable?>,
    private val mFirstDrawable: Drawable?,
    private val mLastDrawable: Drawable?,
    private val mCommonDrawable: Drawable?,
    private val mDrawOnTop: Boolean
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) { // specific view type
        val childType = parent.layoutManager?.getItemViewType(view) ?: return

        // first position
        if (isFirstPosition(view, parent)) {
            if (mFirstDrawable != null) {
                outRect.left = mFirstDrawable.intrinsicWidth
            } else if (mDrawOnTop) {
                val drawableStart = mDividerViewTypeMap[childType] ?: mCommonDrawable ?: return
                outRect.left = drawableStart.intrinsicWidth
            }
            return
        }

        // last position
        if (isLastPosition(view, parent)) {
            if (mLastDrawable != null) {
                outRect.right = mLastDrawable.intrinsicWidth
            }
            if (mDrawOnTop) {
                val drawableEnd = mDividerViewTypeMap[childType] ?: mCommonDrawable ?: return
                outRect.left = drawableEnd.intrinsicWidth
            }
            return
        }
        val drawable = mDividerViewTypeMap[childType] ?: mCommonDrawable ?: return

        if (mDrawOnTop) {
            outRect.left = drawable.intrinsicWidth
        } else {
            outRect.right = drawable.intrinsicWidth
        }
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        var left = 0
        var right = 0
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val childViewType = parent.layoutManager?.getItemViewType(child) ?: return
            val params =
                child.layoutParams as RecyclerView.LayoutParams

            // first position
            if (isFirstPosition(child, parent)) {
                if (mFirstDrawable != null) {
                    right = child.left - params.leftMargin
                    left = right - mFirstDrawable.intrinsicWidth
                    mFirstDrawable.setBounds(left, top, right, bottom)
                    mFirstDrawable.draw(c)
                } else if (mDrawOnTop) {
                    val drawableStart =
                        mDividerViewTypeMap[childViewType] ?: mCommonDrawable ?: return
                    left = child.right + params.rightMargin
                    right = left + drawableStart.intrinsicWidth
                    drawableStart.setBounds(left, top, right, bottom)
                    drawableStart.draw(c)
                }
                return
            }

            // last position
            if (isLastPosition(child, parent)) {
                if (mLastDrawable != null) {
                    left = child.right + params.rightMargin
                    right = left + mLastDrawable.intrinsicWidth
                    mLastDrawable.setBounds(left, top, right, bottom)
                    mLastDrawable.draw(c)
                } else if (mDrawOnTop) {
                    val drawableEnd =
                        mDividerViewTypeMap[childViewType] ?: mCommonDrawable ?: return
                    right = child.left - params.leftMargin
                    left = right - drawableEnd.intrinsicWidth
                    drawableEnd.setBounds(left, top, right, bottom)
                    drawableEnd.draw(c)
                }

                return
            }
            // specific view type
            val drawable = mDividerViewTypeMap[childViewType] ?: mCommonDrawable ?: return
            if (mDrawOnTop) {
                right = child.left - params.leftMargin
                left = right - drawable.intrinsicWidth
            } else {
                left = child.right + params.rightMargin
                right = left + drawable.intrinsicWidth
            }
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(c)
        }
    }

    private fun isFirstPosition(view: View, parent: RecyclerView): Boolean {
        return parent.getChildAdapterPosition(view) == 0
    }

    private fun isLastPosition(view: View, parent: RecyclerView): Boolean {
        return parent.getChildAdapterPosition(view) == parent.adapter?.itemCount ?: 0 - 1
    }

    class Builder(private val mContext: Context) {
        private val mDividerViewTypeMap = SparseArray<Drawable?>()
        private var mFirstDrawable: Drawable? = null
        private var mLastDrawable: Drawable? = null
        private var mCommonDrawable: Drawable? = null
        private var mDrawOnTop = false
        fun drawOnTop(drawOnTop: Boolean): Builder {
            mDrawOnTop = drawOnTop
            return this
        }

        fun common(drawable: Drawable?): Builder {
            mCommonDrawable = drawable
            return this
        }

        fun type(
            types: List<Int>?,
            drawable: Drawable?
        ): Builder {
            if (types == null || drawable == null) {
                return this
            }
            for (type in types) {
                type(type, drawable)
            }
            return this
        }

        fun type(viewType: Int): Builder {
            val a =
                mContext.obtainStyledAttributes(ATTRS)
            val divider = a.getDrawable(0)
            type(viewType, divider)
            a.recycle()
            return this
        }

        fun type(viewType: Int, @DrawableRes drawableResId: Int): Builder {
            mDividerViewTypeMap.put(viewType, ContextCompat.getDrawable(mContext, drawableResId))
            return this
        }

        fun type(
            viewType: Int,
            drawable: Drawable?
        ): Builder {
            mDividerViewTypeMap.put(viewType, drawable)
            return this
        }

        fun first(@DrawableRes drawableResId: Int): Builder {
            first(ContextCompat.getDrawable(mContext, drawableResId))
            return this
        }

        fun first(drawable: Drawable?): Builder {
            mFirstDrawable = drawable
            return this
        }

        fun last(@DrawableRes drawableResId: Int): Builder {
            last(ContextCompat.getDrawable(mContext, drawableResId))
            return this
        }

        fun last(drawable: Drawable?): Builder {
            mLastDrawable = drawable
            return this
        }

        fun create(): HorizontalItemDecoration {
            return HorizontalItemDecoration(
                mDividerViewTypeMap, mFirstDrawable,
                mLastDrawable, mCommonDrawable,
                mDrawOnTop
            )
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}