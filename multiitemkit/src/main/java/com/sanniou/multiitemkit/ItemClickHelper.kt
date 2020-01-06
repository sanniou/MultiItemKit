package com.sanniou.multiitemkit

import androidx.recyclerview.widget.RecyclerView

object ItemClickHelper {

    fun attachToRecyclerView(
        recyclerView: RecyclerView,
        itemClickListener: OnItemClickListener?,
        longPressListener: OnLongPressListener? = null,
        specialViewClickListener: SpecialViewClickListener? = null
    ) {
        val lastListener = recyclerView.getTag(R.id.multiitemkit_tag_touch_listener)
        if (lastListener is BaseRecyclerViewItemTouchListener) {
            lastListener.setClickListener(itemClickListener)
            lastListener.setLongClickListener(longPressListener)
            lastListener.setSpecialViewClickListener(specialViewClickListener)
            return
        }
        if (itemClickListener == null && longPressListener == null && specialViewClickListener == null) {
            return
        }
        val touchListener = BaseRecyclerViewItemTouchListener(
            recyclerView,
            itemClickListener,
            longPressListener,
            specialViewClickListener
        )
        recyclerView.setTag(R.id.multiitemkit_tag_touch_listener, touchListener)
        recyclerView.addOnItemTouchListener(touchListener)
    }
}