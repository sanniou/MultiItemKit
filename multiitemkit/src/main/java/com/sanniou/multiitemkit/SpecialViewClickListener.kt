package com.sanniou.multiitemkit

import com.sanniou.multiitem.AdapterViewHolder

/**
 * itemView 中指定 id 的点击事件，限定在 itemView 的 childView
 */
interface SpecialViewClickListener {
    /**
     * @param holder viewHolder
     * @param viewID 被点击的 ID
     * @return true 拦截点击事件
     */
    fun onSpecialViewClick(holder: AdapterViewHolder, viewID: Int): Boolean

    /**
     * @param itemType itemViewType
     * @return 需要拦截的ids
     */
    fun getSpecialViewIDs(itemType: Int): List<Int>

    /**
     * 点击的边缘 offset ，可以继承后自定义
     */
    fun getClickEdge() = 0
}