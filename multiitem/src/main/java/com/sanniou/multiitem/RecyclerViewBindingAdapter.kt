package com.sanniou.multiitem

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("snap")
fun bindingRecyclerSnap(view: RecyclerView, snap: String) {
    if (snap.isEmpty()) {
        return
    }
    view.onFlingListener = null
    if ("PagerSnapHelper" == snap) {
        PagerSnapHelper().attachToRecyclerView(view)
    } else if ("LinearSnapHelper" == snap) {
        LinearSnapHelper().attachToRecyclerView(view)
    }
}

/**
 * @param data 数据集
 * @param layoutManager LayoutManager 字符串
 * @param orientation orientation 默认是0 ，为了使默认使用 VERTICAL ，这里规定 0 = VERTICAL0 ，1 = HORIZONTAL，那么需要横向时设置 1 即可
 * @param span span 值
 * @param prefetchCount 预取
 * @param isItemPrefetchEnabled 启用预取
 * @param customerLayoutManager 表示使用自定义 LayoutManager ，如果 true 则 LayoutManager 相关属性不生效
 *
 */
@BindingAdapter(
    value = ["data", "layoutManager", "orientation", "span", "prefetchCount", "isItemPrefetchEnabled", "customerLayoutManager","noClick"],
    requireAll = false
)
fun bindingRecyclerAdapter(
    view: RecyclerView,
    data: MultiItemArrayList<DataItem>,
    layoutManager: String?,
    orientation: Int,
    span: Int,
    prefetchCount: Int,
    isItemPrefetchEnabled: Boolean,
    customerLayoutManager: Boolean,
    noClick: Any,
) {
    handleLayoutManager(
        view,
        data,
        layoutManager,
        orientation,
        span,
        isItemPrefetchEnabled,
        prefetchCount,
        customerLayoutManager
    )

    if (view.adapter == null) {
        view.adapter = MultiItemAdapter(data)
    } else {
        val adapter = view.adapter as MultiItemAdapter<DataItem>
        adapter.data = data
    }
}


@BindingAdapter(
    value = ["data", "itemClickListener", "layoutManager", "orientation", "span", "prefetchCount", "isItemPrefetchEnabled", "customerLayoutManager"],
    requireAll = false
)
fun bindingRecyclerClickAdapter(
    view: RecyclerView,
    data: MultiItemArrayList<DataItem>,
    listener: OnItemClickListener?,
    layoutManager: String?,
    orientation: Int,
    span: Int,
    prefetchCount: Int,
    isItemPrefetchEnabled: Boolean,
    customerLayoutManager: Boolean
) {
    handleLayoutManager(
        view,
        data,
        layoutManager,
        orientation,
        span,
        isItemPrefetchEnabled,
        prefetchCount,
        customerLayoutManager
    )

    if (view.adapter == null) {
        view.adapter = ItemClickAdapter(data).also {
            it.itemClickListener = listener
        }
    } else {
        val adapter = view.adapter as ItemClickAdapter<DataItem>
        adapter.itemClickListener = listener
        adapter.data = data
    }
}

fun handleLayoutManager(
    view: RecyclerView,
    data: MultiItemArrayList<DataItem>,
    layoutManager: String?,
    orientation: Int,
    span: Int,
    isItemPrefetchEnabled: Boolean,
    prefetchCount: Int,
    customerLayoutManager: Boolean
) {
    var manager = layoutManager
    // 如果有自己设置 layoutmanager 则不处理
    if (!customerLayoutManager) {
        var layout = view.layoutManager
        manager = when {
            span > 0 -> {
                "GridLayoutManager"
            }
            manager.isNullOrEmpty() -> {
                "LinearLayoutManager"
            }
            else -> {
                // 目前没有其他情况
                "LinearLayoutManager"
            }
        }

        when (manager) {
            "GridLayoutManager" -> if (layout != null && layout.javaClass == GridLayoutManager::class.java) {
                layout.isItemPrefetchEnabled = isItemPrefetchEnabled
                (layout as GridLayoutManager).initialPrefetchItemCount = prefetchCount
                layout.spanCount = span
                layout.orientation =
                    if (orientation == 0) GridLayoutManager.VERTICAL else GridLayoutManager.HORIZONTAL
                layout.spanSizeLookup = ItemSpanSizeLookup(data, span)
            } else {
                layout = GridLayoutManager(
                    view.context, span,
                    if (orientation == 0) GridLayoutManager.VERTICAL else GridLayoutManager.HORIZONTAL,
                    false
                )
                layout.isItemPrefetchEnabled = isItemPrefetchEnabled
                layout.spanSizeLookup = ItemSpanSizeLookup(data, span)
                layout.initialPrefetchItemCount = prefetchCount
                view.layoutManager = layout
            }

            "LinearLayoutManager" ->  // 不能用 instanceof 因为 GridLayoutManager 也满足条件
                if (layout != null && layout.javaClass == LinearLayoutManager::class.java) {
                    (layout as LinearLayoutManager).orientation =
                        if (orientation == 0) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL
                    layout.setItemPrefetchEnabled(true)
                    layout.initialPrefetchItemCount = prefetchCount
                } else {
                    layout = LinearLayoutManager(
                        view.context,
                        if (orientation == 0) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    layout.setItemPrefetchEnabled(true)
                    layout.initialPrefetchItemCount = prefetchCount
                    view.layoutManager = layout
                }
        }
    }
}