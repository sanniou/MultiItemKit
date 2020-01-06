package com.sanniou.multiitemkit.bindingadapter

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitemkit.BR
import com.sanniou.multiitemkit.R
import com.sanniou.multiitemkit.dp2px
import com.sanniou.multiitemkit.drawable.DividerDrawable
import com.sanniou.multiitemkit.helper.replaceView

@BindingAdapter("onBinding")
fun bindingAdapterBindingEcent(view: View?, callback: () -> Unit) {
    callback()
}

@BindingAdapter("onViewBinding")
fun bindingAdapterBindingEcent(view: View, callback: (View) -> Unit) {
    callback(view)
}

@BindingAdapter("item")
fun bindingFrameLayout(view: FrameLayout, item: DataItem) {
    val viewItem = view.getTag(R.id.multiitemkit_tag_key_item)
    if (viewItem === item) {
        return
    }
    val binding = DataBindingUtil.getBinding(view.getChildAt(0)) ?: run {
        if (view.childCount > 0) {
            view.removeAllViews()
        }
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(view.context),
            item.getItemType(),
            view,
            true
        )
    }

    binding.run {
        setVariable(BR.item, item)
        root.setTag(R.id.multiitemkit_tag_key_item, item)
    }
}

@BindingAdapter(value = ["item"], requireAll = false)
fun bindingViewLayout(view: View, item: DataItem) {
    val viewItem = view.getTag(R.id.multiitemkit_tag_key_item)
    if (viewItem === item) {
        return
    }
    val binding = DataBindingUtil.getBinding(view) ?: run {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(view.context),
            item.getItemType(),
            null,
            true
        ).also {
            replaceView(view, it.root)
        }
    }

    binding.setVariable(BR.item, item)
    binding.root.setTag(R.id.multiitemkit_tag_key_item, item)
}

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