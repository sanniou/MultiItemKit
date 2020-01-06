package com.sanniou.multiitemkit.wrapper

import android.view.View
import android.view.View.OnLongClickListener
import com.sanniou.multiitem.WrapperHandler

class ClickWrapperHandler(
    var onClickListener: View.OnClickListener? = null,
    var onLongClickListener: OnLongClickListener? = null
) : WrapperHandler {

    override fun wrapperHandle(view: View) {
        if (onClickListener != null) {
            view.setOnClickListener(onClickListener)
        }
        if (onLongClickListener != null) {
            view.setOnLongClickListener(onLongClickListener)
        }
    }

    override fun restoreHandle(view: View) {
        view.setOnClickListener(null)
        view.setOnLongClickListener(null)
    }
}