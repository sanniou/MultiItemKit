package com.sanniou.multiitemkit

import android.graphics.Rect
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.sanniou.multiitem.AdapterViewHolder
import com.sanniou.multiitem.getAdapterHolder

class BaseRecyclerViewItemTouchListener(
    private val mRecyclerView: RecyclerView,
    private var mItemClickListener: OnItemClickListener?,
    private var mLongPressListener: OnLongPressListener?,
    private var mSpecialViewClickListener: SpecialViewClickListener?
) : OnItemTouchListener {

    private val mGestureDetector: GestureDetectorCompat

    init {
        mGestureDetector = GestureDetectorCompat(
            mRecyclerView.context,
            object : SimpleOnGestureListener() {

                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    handleClickEvent(e)
                    return false
                }

                override fun onLongPress(e: MotionEvent) {
                    mRecyclerView.findChildViewUnder(e.x, e.y)?.let {
                        mLongPressListener?.onLongPress(it.getAdapterHolder())
                    }
                }
            })
        mGestureDetector.setIsLongpressEnabled(true)
    }

    fun setClickListener(itemClickListener: OnItemClickListener?) {
        mItemClickListener = itemClickListener
    }

    fun setLongClickListener(longPressListener: OnLongPressListener?) {
        mLongPressListener = longPressListener
    }

    fun setSpecialViewClickListener(specialViewClickListener: SpecialViewClickListener?) {
        mSpecialViewClickListener = specialViewClickListener
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent) =
        mGestureDetector.onTouchEvent(event)

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    private fun handleClickEvent(event: MotionEvent): Boolean {
        val view =
            mRecyclerView.findChildViewUnder(event.x, event.y) ?: return false
        val holder = view.getAdapterHolder()
        if (holder.adapterPosition == RecyclerView.NO_POSITION) return false

        mSpecialViewClickListener?.let { specialClick ->
            val specialChildViewId = findExactChild(
                holder, view, event.rawX, event.rawY, specialClick
            )
            if (specialChildViewId != View.NO_ID) {
                return specialClick.onSpecialViewClick(holder, specialChildViewId)
            }
        }

        return mItemClickListener?.onItemClick(holder) ?: false
    }

    private fun findExactChild(
        holder: AdapterViewHolder, view: View, x: Float, y: Float,
        specialClick: SpecialViewClickListener
    ): Int {
        if (view !is ViewGroup) {
            return View.NO_ID
        }
        val itemViewType: Int = holder.item.getItemType()
        val specialViewIDs =
            specialClick.getSpecialViewIDs(itemViewType)
        for (specialViewId in specialViewIDs) {
            val specialView = holder.getView<View>(specialViewId)
            if (specialView != null) {
                val viewBounds = Rect()
                specialView.getGlobalVisibleRect(viewBounds)
                val clickEdge = specialClick.getClickEdge()
                if (x >= viewBounds.left - clickEdge && x <= viewBounds.right + clickEdge && y >= viewBounds.top - clickEdge && y <= viewBounds.bottom + clickEdge) {
                    return specialViewId
                }
            }
        }
        return View.NO_ID
    }
}
