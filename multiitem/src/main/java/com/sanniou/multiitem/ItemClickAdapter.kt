package com.sanniou.multiitem

import android.view.View
import android.view.ViewGroup



open class ItemClickAdapter<T : DataItem>(
    items: MultiItemArrayList<T>,
    var itemClickListener: OnItemClickListener? = null
) : MultiItemAdapter<T>(items), View.OnClickListener {

    override fun onClick(v: View) {
        itemClickListener?.onItemClick(v.getAdapterHolder())
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        super.onCreateViewHolder(viewGroup, viewType)
            .also {
                it.itemView.setOnClickListener(this)
            }
}