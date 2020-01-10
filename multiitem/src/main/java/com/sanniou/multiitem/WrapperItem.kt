package com.sanniou.multiitem

import android.view.View
import androidx.annotation.CallSuper

/**
 * 定义一个Item类型，拥有自身的处理器，可以在 bindAdapter 绑定数据时进行额外的处理
 */
interface WrapperDataItem : DataItem {
    /**
     * 实现此方法，提供一个处理器
     */
    val handlers: Array<WrapperHandler>

    @CallSuper
    override fun onBind(holder: AdapterViewHolder) {
        executeHandlers(holder.itemView)
    }

    @CallSuper
    override fun onDetached(holder: AdapterViewHolder) {
        restoreHandlers(holder.itemView)
    }

    fun executeHandlers(itemView: View) {
        for (handler in handlers) {
            handler.wrapperHandle(itemView)
        }
    }

    fun restoreHandlers(itemView: View) {
        for (handler in handlers) {
            handler.restoreHandle(itemView)
        }
    }
}