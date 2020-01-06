package com.sanniou.multiitemkit.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.sanniou.multiitem.AdapterItem
import com.sanniou.multiitem.DataBindingArrayList
import com.sanniou.multiitemkit.ItemSpanSizeLookup
import com.sanniou.multiitemkit.OnItemClickListener
import com.sanniou.multiitemkit.OnLongPressListener
import com.sanniou.multiitemkit.PerfecteAdapter
import com.sanniou.multiitemkit.SpecialViewClickListener
import com.sanniou.multiitemkit.decoration.HorizontalItemDecoration
import com.sanniou.multiitemkit.decoration.VerticalItemDecoration
import com.sanniou.multiitemkit.dp2px
import com.sanniou.multiitemkit.drawable.DividerDrawable
import java.util.ArrayList

/**
 * @param dividerColor   颜色
 * @param dividerHeight  高度
 * @param divider        是否处理分割线，一般不需设置，最少只设置这一个值即可
 * @param noDivider      不处理分割线
 * @param horizontal     方向，默认竖向
 * @param drawOnTop      绘制在View 上边
 * @param drawFirstStart 最上开始绘制
 * @param drawLastEnd    最后是否绘制
 * @param types          指定需要绘制的 ViewType 的集合，传空绘制全部类型
 */
@BindingAdapter(
    value = ["dividerColor", "dividerHeight", "dividerHeightDp", "noDivider", "dividerHorizontal", "drawOnTop", "drawFirstStart", "drawLastEnd", "types"],
    requireAll = false
)
fun bindingRecyclerDivider(
    view: RecyclerView,
    dividerColor: Int,
    dividerHeight: Int,
    dividerHeightDp: Float,
    noDivider: Boolean,
    horizontal: Boolean,
    drawOnTop: Boolean,
    drawFirstStart: Boolean,
    drawLastEnd: Boolean,
    types: List<Int>?
) {
    var height = dividerHeight
    if (noDivider) {
        return
    }
    if (dividerHeightDp != 0F) {
        height = dp2px(dividerHeightDp)
    }
    if (height == 0) {
        height = dp2px(1F)
    }
    val itemDecorationCount = view.itemDecorationCount
    val itemDecorations = ArrayList<ItemDecoration>()

    for (i in 0 until itemDecorationCount) {
        val decoration = view.getItemDecorationAt(i)
        if (decoration is HorizontalItemDecoration
            || decoration is VerticalItemDecoration
        ) {
            itemDecorations.add(decoration)
        }
    }
    if (itemDecorations.isNotEmpty()) {
        for (decoration in itemDecorations) {
            view.removeItemDecoration(decoration)
        }
    }

    val drawable = DividerDrawable(height, dividerColor)
    if (horizontal) {
        view.addItemDecoration(
            HorizontalItemDecoration.Builder(view.context)
                .common(if (types == null) drawable else null)
                .type(types, drawable)
                .first(if (drawFirstStart) drawable else null)
                .last(if (drawLastEnd) drawable else null)
                .drawOnTop(drawOnTop)
                .create()
        )
    } else {
        view.addItemDecoration(
            VerticalItemDecoration.Builder(view.context)
                .type(types, drawable)
                .common(if (types == null) drawable else null)
                .first(if (drawFirstStart) drawable else null)
                .last(if (drawLastEnd) drawable else null)
                .drawOnTop(drawOnTop)
                .create()
        )
    }
}

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
 * @param orientation orientation 默认是0 但是 0=HORIZONTAL,所以使用时 +1，那么需要横向时设置 -1即可
 */
@BindingAdapter(
    value = ["data", "itemClick", "longPressListener", "itemViewClick", "layoutManager", "orientation", "span", "prefetchCount", "isItemPrefetchEnabled", "customerLayoutManager"],
    requireAll = false
)
fun bindingRecyclerAdapter(
    view: RecyclerView,
    data: DataBindingArrayList<AdapterItem>,
    listener: OnItemClickListener?,
    longPressListener: OnLongPressListener?,
    specialViewListener: SpecialViewClickListener?,
    layoutManager: String?,
    orientation: Int,
    span: Int,
    prefetchCount: Int,
    isItemPrefetchEnabled: Boolean,
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
    if (view.adapter == null) {
        val adapter = PerfecteAdapter(data)
        adapter.itemClickListener = listener
        adapter.longPressListener = longPressListener
        adapter.viewClickListener = specialViewListener
        view.adapter = adapter
    } else {
        val adapter = view.adapter as PerfecteAdapter<AdapterItem>
        adapter.itemClickListener = listener
        adapter.longPressListener = longPressListener
        adapter.viewClickListener = specialViewListener
        adapter.data = data
    }
}
