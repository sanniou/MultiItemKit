package com.sanniou.multiitemkit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitem.AdapterViewHolder
import com.sanniou.multiitem.MultiItemAdapter
import com.sanniou.multiitem.MultiItemArrayList

class MultiClickAdapter<T : DataItem>(items: MultiItemArrayList<T>) :
    MultiItemAdapter<T>(items) {

    var itemClickListener: OnItemClickListener? = null
    var longPressListener: OnLongPressListener? = null
    var viewClickListener: SpecialViewClickListener? = null

    private val _itemClickListener: OnItemClickListener =
        OnItemClickListener { itemClickListener?.onItemClick(it) ?: false }
    private val _longPressListener: OnLongPressListener =
        OnLongPressListener { longPressListener?.onLongPress(it) }
    private val _viewClickListener = object : SpecialViewClickListener {

        override fun onSpecialViewClick(holder: AdapterViewHolder, viewID: Int) =
            viewClickListener?.onSpecialViewClick(holder, viewID) ?: false

        override fun getSpecialViewIDs(itemType: Int) =
            viewClickListener?.getSpecialViewIDs(itemType) ?: emptyList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AdapterViewHolder {
        ItemClickHelper.attachToRecyclerView(
            (viewGroup as RecyclerView),
            _itemClickListener,
            _longPressListener,
            _viewClickListener
        )
        return super.onCreateViewHolder(viewGroup, viewType)
    }
}