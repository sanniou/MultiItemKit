package com.sanniou.multiitemkit.extension

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sanniou.multiitem.AdapterViewHolder
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitemkit.R

/**
 * Remove all Views form dataItems
 * @receiver ViewGroup
 * @param dataItems Array<out DataItem>
 */
fun ViewGroup.removeDataItems(vararg dataItems: DataItem) {
    children.filter {
        val viewItem = it.getTag(R.id.multiitemkit_tag_key_item)
        viewItem != null && dataItems.contains(viewItem)
    }.forEach {
        removeView(it)
    }
}

/**
 *
 * Remove all Views form dataItem
 *
 * @receiver ViewGroup
 * @param dataItem DataItem
 */
fun ViewGroup.removeDataItem(dataItem: DataItem) {
    findDataItemView(dataItem).forEach { removeView(it) }
}

/**
 * Find all Views form dataItem
 *
 * @receiver ViewGroup
 * @param dataItem DataItem
 * @return Sequence<View>
 */
fun ViewGroup.findDataItemView(dataItem: DataItem) =
    children.filter {
        val viewItem = it.getTag(R.id.multiitemkit_tag_key_item)
        viewItem === dataItem
    }

/**
 * add 方法，ViewGroup 不会限制 DataItem
 * @receiver ViewGroup
 * @param dataItem DataItem
 * @param layoutParams view 的 LayoutParams，不同于 [setDataItem] ，不可以再次更新
 * @param index view add 时的 index
 */
fun ViewGroup.addDataItem(
    dataItem: DataItem,
    layoutParams: ViewGroup.LayoutParams? = null,
    index: Int = -1
) {
    DataBindingUtil.inflate<ViewDataBinding>(
        LayoutInflater.from(context),
        dataItem.getItemType(),
        if (layoutParams == null) this else null,
        false
    )?.run {
        val holder = AdapterViewHolder(root)
        holder.onBind(dataItem)
        addView(root, index, layoutParams ?: root.layoutParams)
        holder.onAttached()
        root.addOnAttachStateChangeListener(AdaptMultiItemOnAttachStateChangeListener())
        root.setTag(R.id.multiitemkit_tag_key_item, dataItem)
    }
}

/**
 * set 方法，表示 ViewGroup 只有一个 DataItem
 * @receiver ViewGroup
 * @param dataItem DataItem
 * @param layoutParams view 的 LayoutParams，传入 dataItem 相同时可以更新 layoutParams
 * @param index view add 时的 index，和 LayoutParams 不同，不可以更新
 */

fun ViewGroup.setDataItem(
    dataItem: DataItem,
    layoutParams: ViewGroup.LayoutParams? = null,
    index: Int = -1
) {
    val view = this
    val viewItem = view.getTag(R.id.multiitemkit_tag_key_item)

    viewItem?.run {
        // the same dataItem
        if (viewItem === dataItem) {
            // dataItem already added, update layoutParams, and return.
            layoutParams?.let {
                val dataView = findDataItemView(dataItem).firstOrNull() ?: return
                if (layoutParams != dataView.layoutParams) {
                    dataView.layoutParams = layoutParams
                }
            }
            return
        } else {
            // has added another dataItem, remove it
            (viewItem as? DataItem)?.run {
                removeDataItem(this)
            }
        }
    }

    addDataItem(dataItem, layoutParams, index)

    view.setTag(R.id.multiitemkit_tag_key_item, dataItem)
}

/**
 * Replace all oldDataItem to newDataItem
 * @receiver ViewGroup
 * @param oldDataItem DataItem
 * @param newDataItem DataItem
 * @param layoutParams LayoutParams? if null, will use old view LayoutParams
 */
fun ViewGroup.replaceDataItem(
    oldDataItem: DataItem,
    newDataItem: DataItem,
    layoutParams: ViewGroup.LayoutParams? = null
) {

    findDataItemView(oldDataItem).forEachIndexed { index, view ->
        removeViewAt(index)
        addDataItem(newDataItem, layoutParams ?: view.layoutParams, index)
    }
}

