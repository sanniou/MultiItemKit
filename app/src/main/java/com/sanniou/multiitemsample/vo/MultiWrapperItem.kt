package com.sanniou.multiitemsample.vo

import com.sanniou.multiitem.DataItem
import com.sanniou.multiitem.WrapperDataItem
import com.sanniou.multiitem.WrapperHandler
import com.sanniou.multiitemkit.wrapper.MarginWrapperHandler
import com.sanniou.multiitemkit.wrapper.PaddingWrapperHandler
import com.sanniou.multiitemkit.wrapper.RoundWrapperHandler

class MultiWrapperItem(item: DataItem) : WrapperDataItem {
    private val mItem: DataItem = item

    override val handlers: Array<WrapperHandler> =
        arrayOf(RoundWrapperHandler(), MarginWrapperHandler(), PaddingWrapperHandler())

    val roundHandler: RoundWrapperHandler
        get() = handlers[0] as RoundWrapperHandler

    val marginHandler: MarginWrapperHandler
        get() = handlers[1] as MarginWrapperHandler

    val paddingHandler: PaddingWrapperHandler
        get() = handlers[2] as PaddingWrapperHandler

    override fun getItemType(): Int {
        return mItem.getItemType()
    }

    override fun <T : DataItem> getItemData(): T {
        return mItem.getItemData()
    }
}