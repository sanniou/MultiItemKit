package com.sanniou.multiitemkit.wrapper

import android.view.View
import com.sanniou.multiitem.WrapperHandler

class PaddingWrapperHandler : WrapperHandler {
    private val padding = intArrayOf(NONE_SIZE, NONE_SIZE, NONE_SIZE, NONE_SIZE)
    private val savedPadding = intArrayOf(NONE_SIZE, NONE_SIZE, NONE_SIZE, NONE_SIZE)

    fun noSize() {
        padding[0] = NONE_SIZE
        padding[1] = NONE_SIZE
        padding[2] = NONE_SIZE
        padding[3] = NONE_SIZE
    }

    fun padding(padding: Int) {
        this.padding[0] = padding
        this.padding[1] = padding
        this.padding[2] = padding
        this.padding[3] = padding
    }

    fun padding(
        paddingStart: Int = NONE_SIZE,
        paddingTop: Int = NONE_SIZE,
        paddingEnd: Int = NONE_SIZE,
        paddingBottom: Int = NONE_SIZE
    ) {
        padding[0] = paddingStart
        padding[1] = paddingEnd
        padding[2] = paddingTop
        padding[3] = paddingBottom
    }

    private fun rightSize(original: Int, change: Int): Int {
        return if (change == NONE_SIZE) original else change
    }

    private fun noChange() =
        padding[0] == NONE_SIZE &&
            padding[1] == NONE_SIZE &&
            padding[2] == NONE_SIZE &&
            padding[3] == NONE_SIZE

    override fun wrapperHandle(view: View) {
        if (noChange()) {
            return
        }
        savedPadding[0] = view.paddingStart
        savedPadding[1] = view.paddingTop
        savedPadding[2] = view.paddingEnd
        savedPadding[3] = view.paddingBottom

        view.setPaddingRelative(
            rightSize(savedPadding[0], padding[0]),
            rightSize(savedPadding[1], padding[1]),
            rightSize(savedPadding[2], padding[2]),
            rightSize(savedPadding[3], padding[3])
        )
    }

    override fun restoreHandle(view: View) {
        if (noChange()) {
            return
        }
        view.setPaddingRelative(savedPadding[0], savedPadding[1], savedPadding[2], savedPadding[3])
    }

    companion object {
        const val NONE_SIZE = Int.MIN_VALUE
    }
}