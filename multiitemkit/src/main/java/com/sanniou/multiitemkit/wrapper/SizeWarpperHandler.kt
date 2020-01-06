package com.sanniou.multiitemkit.wrapper

import android.view.View
import com.sanniou.multiitem.WrapperHandler

class SizeWarpperHandler : WrapperHandler {

    var height = NONE_SIZE
    var width = NONE_SIZE

    fun height(height: Int) {
        this.height = height
    }

    fun noHeight() {
        height = NONE_SIZE
    }

    fun noWidth() {
        height = NONE_SIZE
    }

    fun noSize() {
        height = NONE_SIZE
        width = NONE_SIZE
    }

    fun width(width: Int) {
        this.width = width
    }

    fun size(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    override fun wrapperHandle(view: View) {
        if (height == NONE_SIZE && width == NONE_SIZE) {
            return
        }
        val params = view.layoutParams ?: return
        if (height != NONE_SIZE) {
            params.height = height
        }
        if (width != NONE_SIZE) {
            params.width = width
        }
        view.layoutParams = params
    }

    companion object {
        const val NONE_SIZE = Int.MIN_VALUE
    }
}