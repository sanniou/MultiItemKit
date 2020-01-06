package com.sanniou.multiitem

import android.view.View

interface WrapperHandler {
    /**
     * 对 View 做自定义处理
     */
    fun wrapperHandle(view: View)

    fun restoreHandle(view: View)
}