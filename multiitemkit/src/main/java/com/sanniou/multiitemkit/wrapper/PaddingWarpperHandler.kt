package com.sanniou.multiitemkit.wrapper

import android.view.View
import com.sanniou.multiitem.WrapperHandler

class PaddingWarpperHandler : WrapperHandler {
    private var paddingStart = NONE_SIZE
    private var paddingEnd = NONE_SIZE
    private var paddingTop = NONE_SIZE
    private var paddingBottom = NONE_SIZE

    fun noSize() {
        paddingStart = NONE_SIZE
        paddingEnd = NONE_SIZE
        paddingTop = NONE_SIZE
        paddingBottom = NONE_SIZE
    }

    fun padding(padding: Int) {
        paddingStart = padding
        paddingEnd = padding
        paddingTop = padding
        paddingBottom = padding
    }

    fun padding(
        paddingStart: Int, paddingEnd: Int = NONE_SIZE, paddingTop: Int = NONE_SIZE,
        paddingBottom: Int = NONE_SIZE
    ) {
        this.paddingStart = paddingStart
        this.paddingEnd = paddingEnd
        this.paddingTop = paddingTop
        this.paddingBottom = paddingBottom
    }

    override fun wrapperHandle(view: View) {
        if (paddingStart == NONE_SIZE && paddingEnd == NONE_SIZE && paddingTop == NONE_SIZE && paddingBottom == NONE_SIZE) {
            return
        }
        view.setPadding(
            checkSize(view.paddingStart, paddingStart),
            checkSize(view.paddingTop, paddingTop),
            checkSize(view.paddingEnd, paddingEnd),
            checkSize(view.paddingBottom, paddingBottom)
        )
    }

    private fun checkSize(original: Int, change: Int): Int {
        return if (change == NONE_SIZE) original else change
    }

    companion object {
        const val NONE_SIZE = Int.MIN_VALUE
    }
}