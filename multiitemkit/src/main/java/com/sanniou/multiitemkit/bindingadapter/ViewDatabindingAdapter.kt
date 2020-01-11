package com.sanniou.multiitemkit.bindingadapter

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitemkit.dp2px
import com.sanniou.multiitemkit.drawable.DividerDrawable
import com.sanniou.multiitemkit.extension.replaceBy
import com.sanniou.multiitemkit.extension.setDataItem

@BindingAdapter("onBinding")
fun bindingAdapterBindingEcent(view: View?, callback: () -> Unit) =
    callback()

@BindingAdapter("onViewBinding")
fun bindingAdapterBindingEcent(view: View, callback: (View) -> Unit) =
    callback(view)

@BindingAdapter("item")
fun bindingFrameLayout(view: FrameLayout, item: DataItem) =
    view.setDataItem(item)

@BindingAdapter(value = ["item"], requireAll = false)
fun bindingViewLayout(view: View, item: DataItem) =
    view.replaceBy(item)

@BindingAdapter(
    value = ["dividerHeight", "dividerHeightDP", "dividerColor", "dividerBackgroundColor", "dividerPadding", "dividerPaddingDP"],
    requireAll = false
)
fun bindingViewDivider(
    view: View,
    height: Int,
    heightDp: Float,
    @ColorInt color: Int,
    @ColorInt backgroundColor: Int,
    padding: Int,
    paddingDP: Float
) {
    var heightPx = height
    var paddingPx = padding
    if (heightDp != 0F) {
        heightPx = dp2px(heightDp)
    }
    if (paddingDP != 0F) {
        paddingPx = dp2px(paddingDP)
    }
    view.background = DividerDrawable(heightPx, color, paddingPx.toFloat(), backgroundColor)
}
