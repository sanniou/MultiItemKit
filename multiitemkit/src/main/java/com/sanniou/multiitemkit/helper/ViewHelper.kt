package com.sanniou.multiitemkit.helper

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.sanniou.multiitemkit.R

fun setBackgroundKeepingPadding(view: View, drawable: Drawable?) {
    val padding = intArrayOf(
        view.paddingLeft, view.paddingTop,
        view.paddingRight, view.paddingBottom
    )
    view.background = drawable
    view.setPadding(padding[0], padding[1], padding[2], padding[3])
}

fun replaceView(fromView: View, toView: View): Boolean {

    if (fromView === toView) {
        return true
    }
    val params = fromView.layoutParams
    val parentLayout = if (fromView.parent != null) {
        fromView.parent as ViewGroup
    } else {
        fromView.rootView.findViewById(R.id.content)
    }

    val viewIndex = parentLayout.children.indexOfFirst {
        it == fromView
    }

    if (viewIndex == -1) {
        return false
    }

    val parent = toView.parent as? ViewGroup
    // remove view before add
    parent?.removeView(toView)
    // replace = remove + add
    toView.id = fromView.id
    parentLayout.removeViewAt(viewIndex)
    parentLayout.addView(toView, viewIndex, params)
    return true
}
