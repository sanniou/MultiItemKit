package com.sanniou.multiitem

import androidx.databinding.ListChangeRegistry
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import java.util.ArrayList

/**
 * The original [ObservableArrayList] can't be inherited, so copy the code directly and extend the move change and other methods, which is more suitable for the animation display of RecyclerView.
 *
 */
open class MultiItemArrayList<T> : ArrayList<T>(), ObservableList<T> {

    @Transient
    private var mListeners: ListChangeRegistry? = ListChangeRegistry()

    override fun addOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        if (mListeners == null) {
            mListeners = ListChangeRegistry()
        }
        mListeners?.add(listener)
    }

    override fun removeOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        mListeners?.remove(listener)
    }

    override fun add(element: T): Boolean {
        super.add(element)
        notifyAdd(size - 1, 1)
        return true
    }

    override fun add(index: Int, element: T) {
        super.add(index, element)
        notifyAdd(index, 1)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val oldSize = size
        val added = super.addAll(elements)
        if (added) {
            notifyAdd(oldSize, size - oldSize)
        }
        return added
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val added = super.addAll(index, elements)
        if (added) {
            notifyAdd(index, elements.size)
        }
        return added
    }

    override fun clear() {
        val oldSize = size
        super.clear()
        if (oldSize != 0) {
            notifyRemove(0, oldSize)
        }
    }

    override fun removeAt(index: Int): T {
        val `val` = super.removeAt(index)
        notifyRemove(index, 1)
        return `val`
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        return if (index >= 0) {
            removeAt(index)
            true
        } else {
            false
        }
    }

    override fun set(index: Int, element: T): T {
        val `val` = super.set(index, element)
        mListeners?.notifyChanged(this, index, 1)
        return `val`
    }

    public override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex)
        notifyRemove(fromIndex, toIndex - fromIndex)
    }

    private fun notifyAdd(start: Int, count: Int) {
        mListeners?.notifyInserted(this, start, count)
    }

    private fun notifyRemove(start: Int, count: Int) {
        if (mListeners != null) {
            mListeners!!.notifyRemoved(this, start, count)
        }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return super.removeAll(elements)
            .also { mListeners?.notifyChanged(this) }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return super.retainAll(elements)
            .also { mListeners?.notifyChanged(this) }
    }

    fun swap(left: Int, right: Int) {
        super.set(left, super.set(right, super.get(left)))
    }

    fun change(position: Int) {
        mListeners?.notifyChanged(this, position, 1)
    }

    fun move(from: Int, to: Int) {
        val remove = super.removeAt(from)
        super.add(to, remove)
        mListeners?.notifyMoved(this, from, to, 1)
    }
}