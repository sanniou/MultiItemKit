package com.sanniou.multiitemkit;

import com.sanniou.multiitem.AdapterViewHolder

fun interface OnItemClickListener {
    fun onItemClick(holder: AdapterViewHolder): Boolean
}