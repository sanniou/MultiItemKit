package com.sanniou.multiitem

import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup

class ItemSpanSizeLookup(private val data: List<DataItem>, private val span: Int) :
    SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        if (data.size > position) {
            val itemSpan = data[position].getGridSpan()
            return if (itemSpan == -1) span else itemSpan
        }
        return span
    }
}