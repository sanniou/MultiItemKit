package com.sanniou.multiitemkit.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.sanniou.multiitemkit.ItemClickHelper
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
    if (noDivider) {
        return
    }
    val height = if (dividerHeightDp != 0F) dp2px(dividerHeightDp) else dividerHeight

    if (height == 0) {
        return
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
 *
 * @param view RecyclerView
 * @param itemClickListener item 点击监听
 * @param longPressListener item 长按监听
 * @param viewListener item 指定 id 的 view 点击监听
 */
@BindingAdapter(
    value = ["itemClickListener", "longPressListener", "viewClickListener"],
    requireAll = false
)
fun bindingRecyclerClick(
    view: RecyclerView,
    itemClickListener: OnItemClickListener?,
    longPressListener: OnLongPressListener?,
    viewListener: SpecialViewClickListener?
) {
    ItemClickHelper.attachToRecyclerView(view, itemClickListener, longPressListener, viewListener)
}