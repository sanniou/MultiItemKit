package com.sanniou.multiitemkit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanniou.multiitem.AdapterItem
import com.sanniou.multiitem.AdapterViewHolder
import com.sanniou.multiitem.DataBindingArrayList
import com.sanniou.multiitem.MultiItemAdapter

class PerfecteAdapter<T : AdapterItem>(items: DataBindingArrayList<T>) :
    MultiItemAdapter<T>(items) {

    var itemClickListener: OnItemClickListener? = null
    var longPressListener: OnLongPressListener? = null
    var viewClickListener: SpecialViewClickListener? = null

    private val _Item_clickListener: OnItemClickListener = { itemClickListener?.invoke(it) ?: false }
    private val _longPressListener: OnLongPressListener? = { longPressListener?.invoke(it) }
    private val _viewClickListener = object : SpecialViewClickListener {

        override fun onSpecialViewClick(holder: AdapterViewHolder, viewID: Int) =
            viewClickListener?.onSpecialViewClick(holder, viewID) ?: false

        override fun getSpecialViewIDs(itemType: Int) =
            viewClickListener?.getSpecialViewIDs(itemType) ?: emptyList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AdapterViewHolder {
        ItemClickHelper.attachToRecyclerView(
            (viewGroup as RecyclerView),
            _Item_clickListener,
            _longPressListener,
            _viewClickListener
        )
        return super.onCreateViewHolder(viewGroup, viewType)
    }
}