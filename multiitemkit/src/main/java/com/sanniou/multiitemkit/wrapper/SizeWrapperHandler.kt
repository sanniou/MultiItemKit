package com.sanniou.multiitemkit.wrapper

import android.view.View
import com.sanniou.multiitem.WrapperHandler

class SizeWrapperHandler : WrapperHandler {
    private val size = intArrayOf(
        MarginWrapperHandler.NONE_SIZE,
        MarginWrapperHandler.NONE_SIZE
    )
    private val savedsize = intArrayOf(
        MarginWrapperHandler.NONE_SIZE,
        MarginWrapperHandler.NONE_SIZE
    )

    fun height(height: Int) {
        size[1] = height
    }

    fun noHeight() {
        size[1] = NONE_SIZE
    }

    fun width(width: Int) {
        size[0] = width
    }

    fun noWidth() {
        size[0] = NONE_SIZE
    }

    fun noSize() {
        size[0] = NONE_SIZE
        size[1] = NONE_SIZE
    }

    fun size(width: Int, height: Int) {
        size[0] = width
        size[1] = height
    }

    private fun noChange() = size[0] == NONE_SIZE && size[1] == NONE_SIZE

    override fun wrapperHandle(view: View) {
        if (noChange()) {
            return
        }
        val params = view.layoutParams ?: return
        savedsize[0] = params.width
        savedsize[1] = params.height
        if (size[0] != NONE_SIZE) {
            params.width = size[0]
        }
        if (size[1] != NONE_SIZE) {
            params.height = size[1]
        }
    }

    override fun restoreHandle(view: View) {
        if (noChange()) {
            return
        }
        val params = view.layoutParams ?: return
        params.width = savedsize[0]
        params.height = savedsize[1]
    }

    companion object {
        const val NONE_SIZE = Int.MIN_VALUE
    }
}