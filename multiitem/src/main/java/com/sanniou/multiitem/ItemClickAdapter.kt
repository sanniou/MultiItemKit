package com.sanniou.multiitem

import android.view.View
import android.view.ViewGroup

typealias OnItemClickListener = (holder: AdapterViewHolder) -> Unit

open class ItemClickAdapter<T : AdapterItem>(
    items: DataBindingArrayList<T>,
    var itemClickListener: OnItemClickListener? = null
) : MultiItemAdapter<T>(items), View.OnClickListener {

    override fun onClick(v: View) {
        itemClickListener?.invoke(v.getAdapterHolder())
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        super.onCreateViewHolder(viewGroup, viewType)
            .also {
                it.itemView.setOnClickListener(this)
            }
}