package com.sanniou.multiitemkit

import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.sanniou.multiitem.AdapterItem

class ItemSpanSizeLookup(private val data: List<AdapterItem>, private val span: Int) :
    SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        if (data.size > position) {
            val itemSpan = data[position].getGridSpan()
            return if (itemSpan == -1) span else itemSpan
        }
        return span
    }
}