package com.sanniou.multiitemkit.wrapper

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import com.sanniou.multiitem.WrapperHandler

class MarginWarpperHandler : WrapperHandler {
    private var marginStart = NONE_SIZE
    private var marginEnd = NONE_SIZE
    private var marginTop = NONE_SIZE
    private var marginBottom = NONE_SIZE

    fun noSize() {
        marginStart = NONE_SIZE
        marginEnd = NONE_SIZE
        marginTop = NONE_SIZE
        marginBottom = NONE_SIZE
    }

    fun margin(margin: Int) {
        marginStart = margin
        marginEnd = margin
        marginTop = margin
        marginBottom = margin
    }

    fun margin(
        marginBottom: Int,
        marginStart: Int = NONE_SIZE, marginEnd: Int = NONE_SIZE, marginTop: Int = NONE_SIZE
    ) {
        this.marginStart = marginStart
        this.marginEnd = marginEnd
        this.marginTop = marginTop
        this.marginBottom = marginBottom
    }

    override fun wrapperHandle(view: View) {
        if (marginStart == NONE_SIZE && marginEnd == NONE_SIZE && marginTop == NONE_SIZE && marginBottom == NONE_SIZE) {
            return
        }
        val params = view.layoutParams
        if (params is MarginLayoutParams) {
            params.setMargins(
                checkSize(params.marginStart, marginStart),
                checkSize(params.topMargin, marginTop),
                checkSize(params.marginEnd, marginEnd),
                checkSize(params.bottomMargin, marginBottom)
            )
            view.layoutParams = params
        }
    }

    private fun checkSize(original: Int, change: Int): Int {
        return if (change == NONE_SIZE) original else change
    }

    companion object {
        const val NONE_SIZE = Int.MIN_VALUE
    }
}