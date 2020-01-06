package com.sanniou.multiitemkit.vo

import com.sanniou.multiitem.DataItem
import com.sanniou.multiitemkit.R

class DividerItem(
    var color: Int = 0,
    var backgroundColor: Int = 0,
    var height: Int = 0,
    var heightDP: Float = 0F,
    var padding: Int = 0,
    var paddingDP: Float = 0F
) : DataItem {

    override fun getItemType() = R.layout.multiitemkit_item_divider
}