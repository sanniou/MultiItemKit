package com.sanniou.multiitemkit.extension

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sanniou.multiitem.AdapterViewHolder
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitemkit.R
import com.sanniou.multiitemkit.helper.replaceView

fun View.replaceBy(dataItem: DataItem) {
    val view = this
    val viewItem = view.getTag(R.id.multiitemkit_tag_key_item)
    if (viewItem === dataItem) {
        return
    }

    DataBindingUtil.inflate<ViewDataBinding>(
        LayoutInflater.from(view.context),
        dataItem.getItemType(),
        null,
        true
    ).apply {
        replaceView(view, root)

        val holder = AdapterViewHolder(root)
        holder.onBind(dataItem)
        holder.onAttached()
        root.addOnAttachStateChangeListener(AdaptMultiItemOnAttachStateChangeListener())
        root.setTag(R.id.multiitemkit_tag_key_item, dataItem)
    }
}