package com.sanniou.multiitemkit.vo

import androidx.databinding.ObservableField
import com.sanniou.multiitemkit.R
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitem.AdapterViewHolder

typealias  OnLoadListener = (callBack: LoadCallBack) -> Unit

open class LoadMoreItem(var onLoadListener: OnLoadListener) : DataItem, LoadCallBack {

    var status = ObservableField(STATE_READY)
    var text = ObservableField<String>()
    var autoLoad = true

    init {
        ready()
    }

    /**
     * 初始状态，只有此时才响应加载
     */
    fun ready() {
        onStatusChange(STATE_READY)
    }

    private fun onStatusChange(nextStatus: Int) {
        status.set(nextStatus)
        text.set(string)
    }

    fun load() {
        if (status.get() != STATE_READY) {
            return
        }
        onLoadListener(this)
        onStatusChange(STATE_LOADING)
    }

    fun reload() {
        if (status.get() == STATE_NO_MORE || status.get() == STATE_FAILED || status.get() == STATE_READY) {
            onStatusChange(STATE_READY)
            load()
        }
    }

    private val string: String
        get() {
            return when (status.get()) {
                STATE_READY -> readyText
                STATE_LOADING -> loadingText
                STATE_FAILED -> failedText
                STATE_SUCCESS -> successText
                STATE_NO_MORE -> noMoreText
                else -> ""
            }
        }

    override fun loadSuccess(hasMore: Boolean) {
        if (hasMore) {
            onStatusChange(STATE_SUCCESS)
            // 可以补充一个切换过程
            onStatusChange(STATE_READY)
        } else {
            onStatusChange(STATE_NO_MORE)
        }
    }

    override fun loadFailed() {
        onStatusChange(STATE_FAILED)
    }

    override fun getItemType() = defaultLayout

    override fun onBind(holder: AdapterViewHolder) {
        if (autoLoad) {
            load()
        }
    }

    companion object {

        var defaultLayout = R.layout.multiitemkit_item_loadmore
        var readyText = "待加载"
        var loadingText = "加载中"
        var failedText = "加载失败"
        var successText = "加载成功"
        var noMoreText = "没有更多了"

        private const val STATE_READY = 0
        private const val STATE_NO_MORE = 1
        private const val STATE_LOADING = 2
        private const val STATE_FAILED = 3
        private const val STATE_SUCCESS = 4
    }
}

interface LoadCallBack {
    fun loadSuccess(hasMore: Boolean)
    fun loadFailed()
}
