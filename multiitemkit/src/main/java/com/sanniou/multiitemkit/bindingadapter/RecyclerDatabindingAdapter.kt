package com.sanniou.multiitemkit.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitem.MultiItemArrayList
import com.sanniou.multiitem.handleLayoutManager
import com.sanniou.multiitemkit.MultiClickAdapter
import com.sanniou.multiitemkit.OnItemClickListener
import com.sanniou.multiitemkit.OnLongPressListener
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
 * @param drawOnTop      绘制在 View 上边
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

/**
 * @param data 数据集
 * @param itemClickListener item 点击监听
 * @param longPressListener item 长按监听
 * @param viewListener item 指定 id 的 view 点击监听
 * @param layoutManager LayoutManager 字符串
 * @param orientation orientation 默认是0 ，为了使默认使用 VERTICAL ，这里规定 0 = VERTICAL0 ，1 = HORIZONTAL，那么需要横向时设置 1 即可
 * @param span span 值
 * @param prefetchCount 预取
 * @param isItemPrefetchEnabled 启用预取
 * @param customerLayoutManager 表示使用自定义 LayoutManager ，如果 true 则 LayoutManager 相关属性不生效
 *
 */
@BindingAdapter(
    value = ["data", "itemClickListener", "longPressListener", "viewClickListener", "layoutManager", "orientation", "span", "prefetchCount", "isItemPrefetchEnabled", "customerLayoutManager"],
    requireAll = false
)
fun bindingRecyclerAdapter(
    view: RecyclerView,
    data: MultiItemArrayList<DataItem>,
    itemClickListener: OnItemClickListener?,
    longPressListener: OnLongPressListener?,
    viewListener: SpecialViewClickListener?,
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
        view.adapter = MultiClickAdapter(data).also {
            it.itemClickListener = itemClickListener
            it.longPressListener = longPressListener
            it.viewClickListener = viewListener
        }
    } else {
        val adapter = view.adapter as MultiClickAdapter<DataItem>
        adapter.itemClickListener = itemClickListener
        adapter.longPressListener = longPressListener
        adapter.viewClickListener = viewListener
        adapter.data = data
    }
}

