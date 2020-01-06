package com.sanniou.multiitem

import android.view.View

/**
 * 定义一个Item类型，拥有自身的处理器，可以在 bindAdapter 绑定数据时进行额外的处理
 *
 * @author jichang
 * @date 2018/9/6
 */
interface WrapperAdapterItem : AdapterItem {
    /**
     * 实现此方法，提供一个处理器
     */
    val handlers: Array<WrapperHandler>

    fun executeHankers(itemView: View) {
        for (handler in handlers) {
            handler.wrapperHandle(itemView)
        }
    }
}