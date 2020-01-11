package com.sanniou.multiitemkit.extension

import android.view.View
import com.sanniou.multiitem.getAdapterHolder

class AdaptMultiItemOnAttachStateChangeListener : View.OnAttachStateChangeListener {
    override fun onViewDetachedFromWindow(v: View) {
        v.getAdapterHolder().onDetached()
        v.getAdapterHolder().onRecycled()
        v.removeOnAttachStateChangeListener(this)
    }

    override fun onViewAttachedToWindow(v: View) {
    }
}