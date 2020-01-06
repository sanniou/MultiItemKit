package com.sanniou.multiitemkit.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import java.util.HashMap

/**
 * 垂直分割线
 */
class VerticalItemDecoration(
    private val mDividerViewTypeMap: Map<Int, Drawable?>,
    private val mFirstDrawable: Drawable?,
    private val mLastDrawable: Drawable?,
    private val mCommonDrawable: Drawable?,
    private val mDrawOnTop: Boolean
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) { // last position
        if (isLastPosition(view, parent)) {
            if (mLastDrawable != null) {
                if (mDrawOnTop) {
                    outRect.top = mLastDrawable.intrinsicHeight
                } else {
                    outRect.bottom = mLastDrawable.intrinsicHeight
                }
            }
            return
        }
        // specific view type
        val childType = parent.layoutManager!!.getItemViewType(view)
        var drawable = mDividerViewTypeMap[childType]
        if (drawable == null) {
            drawable = mCommonDrawable
        }
        if (drawable != null) {
            if (mDrawOnTop) {
                outRect.top = drawable.intrinsicHeight
            } else {
                outRect.bottom = drawable.intrinsicHeight
            }
        }
        // first position
        if (isFirstPosition(view, parent) && mFirstDrawable != null) {
            outRect.top = mFirstDrawable.intrinsicHeight
        }
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val childViewType = parent.layoutManager!!.getItemViewType(child)
            val params =
                child.layoutParams as RecyclerView.LayoutParams
            // last position
            if (isLastPosition(child, parent)) {
                if (mLastDrawable != null) {
                    val bottom: Int
                    val top: Int
                    if (mDrawOnTop) {
                        bottom = child.top + params.topMargin
                        top = bottom + mLastDrawable.intrinsicHeight
                    } else {
                        top = child.bottom + params.bottomMargin
                        bottom = top + mLastDrawable.intrinsicHeight
                    }
                    mLastDrawable.setBounds(left, top, right, bottom)
                    mLastDrawable.draw(c)
                }
                return
            }
            // specific view type
            var drawable = mDividerViewTypeMap[childViewType]
            if (drawable == null) {
                drawable = mCommonDrawable
            }
            if (drawable != null) {
                var bottom: Int
                var top: Int
                if (mDrawOnTop) {
                    bottom = child.top + params.topMargin
                    top = bottom + drawable.intrinsicHeight
                } else {
                    top = child.bottom + params.bottomMargin
                    bottom = top + drawable.intrinsicHeight
                }
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(c)
            }
            // first position
            if (isFirstPosition(child, parent) && mFirstDrawable != null) {
                val bottom = child.top - params.topMargin
                val top = bottom - mFirstDrawable.intrinsicHeight
                mFirstDrawable.setBounds(left, top, right, bottom)
                mFirstDrawable.draw(c)
            }
        }
    }

    private fun isFirstPosition(view: View, parent: RecyclerView): Boolean {
        return parent.getChildAdapterPosition(view) == 0
    }

    private fun isLastPosition(view: View, parent: RecyclerView): Boolean {
        return parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1
    }

    class Builder(private val mContext: Context) {
        private val mDividerViewTypeMap: MutableMap<Int, Drawable?> =
            HashMap()
        private var mFirstDrawable: Drawable? = null
        private var mLastDrawable: Drawable? = null
        private var mCommonDrawable: Drawable? = null
        private var mDrawOnTop = false
        fun type(viewType: Int): Builder {
            val a =
                mContext.obtainStyledAttributes(ATTRS)
            val divider = a.getDrawable(0)
            type(viewType, divider)
            a.recycle()
            return this
        }

        fun drawOnTop(drawOnTop: Boolean): Builder {
            mDrawOnTop = drawOnTop
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

        fun type(viewType: Int, @DrawableRes drawableResId: Int): Builder {
            mDividerViewTypeMap[viewType] = ContextCompat.getDrawable(mContext, drawableResId)
            return this
        }

        fun type(
            viewType: Int,
            drawable: Drawable?
        ): Builder {
            mDividerViewTypeMap[viewType] = drawable
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

        fun common(@DrawableRes drawableResId: Int): Builder {
            common(ContextCompat.getDrawable(mContext, drawableResId))
            return this
        }

        fun common(drawable: Drawable?): Builder {
            mCommonDrawable = drawable
            return this
        }

        fun create(): VerticalItemDecoration {
            return VerticalItemDecoration(
                mDividerViewTypeMap, mFirstDrawable, mLastDrawable,
                mCommonDrawable, mDrawOnTop
            )
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}