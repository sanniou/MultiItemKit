package com.sanniou.multiitemkit.wrapper

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import com.sanniou.multiitem.WrapperHandler

class MarginWrapperHandler : WrapperHandler {
    private val margins = intArrayOf(NONE_SIZE, NONE_SIZE, NONE_SIZE, NONE_SIZE)
    private val savedMargins = intArrayOf(NONE_SIZE, NONE_SIZE, NONE_SIZE, NONE_SIZE)

    fun noSize() {
        margins[0] = NONE_SIZE
        margins[1] = NONE_SIZE
        margins[2] = NONE_SIZE
        margins[3] = NONE_SIZE
    }

    fun margin(margin: Int) {
        margins[0] = margin
        margins[1] = margin
        margins[2] = margin
        margins[3] = margin
    }

    fun margin(
        marginStart: Int = NONE_SIZE,
        marginTop: Int = NONE_SIZE,
        marginEnd: Int = NONE_SIZE,
        marginBottom: Int = NONE_SIZE
    ) {
        margins[0] = marginStart
        margins[1] = marginEnd
        margins[2] = marginTop
        margins[3] = marginBottom
    }

    private fun noChange() =
        margins[0] == NONE_SIZE &&
            margins[1] == NONE_SIZE &&
            margins[2] == NONE_SIZE &&
            margins[3] == NONE_SIZE

    private fun rightSize(original: Int, change: Int): Int {
        return if (change == NONE_SIZE) original else change
    }

    override fun wrapperHandle(view: View) {
        if (noChange()) {
            return
        }
        val params = view.layoutParams
        if (params is MarginLayoutParams) {
            savedMargins[0] = params.marginStart
            savedMargins[1] = params.topMargin
            savedMargins[2] = params.marginEnd
            savedMargins[3] = params.bottomMargin
            params.marginStart = rightSize(savedMargins[0], margins[0])
            params.topMargin = rightSize(savedMargins[1], margins[1])
            params.marginEnd = rightSize(savedMargins[2], margins[2])
            params.bottomMargin = rightSize(savedMargins[3], margins[3])
        }
    }

    override fun restoreHandle(view: View) {
        if (noChange()) {
            return
        }
        val params = view.layoutParams
        if (params is MarginLayoutParams) {
            params.marginStart = savedMargins[0]
            params.topMargin = savedMargins[1]
            params.marginEnd = savedMargins[2]
            params.bottomMargin = savedMargins[3]
        }
    }

    companion object {
        const val NONE_SIZE = Int.MIN_VALUE
    }
}