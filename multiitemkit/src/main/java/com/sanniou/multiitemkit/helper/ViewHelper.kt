package com.sanniou.multiitemkit.helper

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup

fun setBackgroundKeepingPadding(view: View, drawable: Drawable?) {
    val padding = intArrayOf(
        view.paddingLeft, view.paddingTop,
        view.paddingRight, view.paddingBottom
    )
    view.background = drawable
    view.setPadding(padding[0], padding[1], padding[2], padding[3])
}

fun replaceView(oldView: View, newView: View): Boolean {

    if (oldView === newView) {
        return true
    }
    val params = oldView.layoutParams
    val parentLayout = (oldView.parent as? ViewGroup) ?: return false

    val viewIndex = parentLayout.indexOfChild(oldView)

    if (viewIndex == -1) {
        return false
    }

    val parent = newView.parent as? ViewGroup
    // remove view before add
    parent?.removeView(newView)
    // replace = remove + add
    newView.id = oldView.id
    parentLayout.removeViewAt(viewIndex)
    parentLayout.addView(newView, viewIndex, params)
    return true
}
