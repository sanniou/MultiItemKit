package com.sanniou.multiitemkit.vo

import com.sanniou.multiitem.DataItem
import com.sanniou.multiitemkit.R

/**
 * 用于recycler list 中删除某个item 而不想影响其他 index 硬编码的部分时，不占用任何显示空间
 *
 */

class NonItem : DataItem {
    override fun getItemType() = R.layout.multiitemkit_item_non
}