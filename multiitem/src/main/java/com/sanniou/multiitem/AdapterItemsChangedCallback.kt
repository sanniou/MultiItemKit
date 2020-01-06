package com.sanniou.multiitem

import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView

/**
 * Binding the data relationship between Adapter and ObservableList
 */
class AdapterItemsChangedCallback<T : AdapterItem>(private val adapter: RecyclerView.Adapter<*>) :
    ObservableList.OnListChangedCallback<ObservableList<T>>() {

    override fun onChanged(sender: ObservableList<T>) {
        val adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onItemRangeChanged(
        sender: ObservableList<T>,
        positionStart: Int,
        itemCount: Int
    ) {
        val adapter = adapter
        adapter.notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeInserted(
        sender: ObservableList<T>,
        positionStart: Int,
        itemCount: Int
    ) {
        val adapter = adapter
        adapter.notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun onItemRangeMoved(
        sender: ObservableList<T>,
        fromPosition: Int,
        toPosition: Int,
        itemCount: Int
    ) {
        val adapter = adapter
        for (i in 0 until itemCount) {
            adapter.notifyItemMoved(fromPosition + i, toPosition + i)
        }
    }

    override fun onItemRangeRemoved(
        sender: ObservableList<T>,
        positionStart: Int,
        itemCount: Int
    ) {
        val adapter = adapter
        adapter.notifyItemRangeRemoved(positionStart, itemCount)
    }
}