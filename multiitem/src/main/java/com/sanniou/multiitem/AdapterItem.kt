package com.sanniou.multiitem

/**
 * Item interface of ObservableListAdapter
 */
var defaultItemID = BR.item

interface AdapterItem {

    /**
     * when some warp data
     */
    fun <T : AdapterItem> getItemData(): T {
        return this as T
    }

    /**
     * DataBinding layout variable id, default call it item
     */
    fun getVariableId() = defaultItemID

    /**
     * Item type is also the layout id
     *
     */
    fun getItemType(): Int

    /**
     * Span value for gridlayoutmanager. Default is -1. Return -1 to set full screen. Overwrite as needed.
     */
    fun getGridSpan(): Int {
        return -1
    }

    /**
     * item onBind
     */
    fun onBind(holder: AdapterViewHolder) {}

    /**
     * item onRecycler
     */
    fun onRecycler(holder: AdapterViewHolder) {}

    /**
     * item onAttached
     */
    fun onAttached(holder: AdapterViewHolder) {}

    /**
     * item onDetached
     */
    fun onDetached(holder: AdapterViewHolder) {}

    fun onRecycled(holder: AdapterViewHolder) {}

    fun onFailedToRecycleView(holder: AdapterViewHolder) = false
}