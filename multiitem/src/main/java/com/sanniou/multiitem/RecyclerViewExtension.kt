package com.sanniou.multiitem

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.rebind(position: Int) {
    (findViewHolderForAdapterPosition(position) as? AdapterViewHolder)?.let {
        it.rebind()
    }
}
