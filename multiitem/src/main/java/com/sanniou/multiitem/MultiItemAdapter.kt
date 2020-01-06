package com.sanniou.multiitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class MultiItemAdapter<T : AdapterItem>(
    items: DataBindingArrayList<T>
) : RecyclerView.Adapter<AdapterViewHolder>() {

    private var mCallback = AdapterItemsChangedCallback<T>(this)

    var data: DataBindingArrayList<T> = items
        set(value) {
            if (field == value) {
                return
            }
            field.removeOnListChangedCallback(mCallback)
            value.addOnListChangedCallback(mCallback)
            field = value
            notifyDataSetChanged()
        }

    init {
        items.addOnListChangedCallback(mCallback)
    }

    protected fun getItem(i: Int): T {
        return data[i]
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemType()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(viewGroup.context), viewType, viewGroup,
            false
        )
        val itemView = binding.root
        return AdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(bindViewHolder: AdapterViewHolder, position: Int) {
        val item = getItem(position)
        bindViewHolder.onBind(item)
    }

    override fun onFailedToRecycleView(holder: AdapterViewHolder): Boolean {
        return holder.onFailedToRecycleView()
    }

    override fun onViewAttachedToWindow(holder: AdapterViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttached()
    }

    override fun onViewDetachedFromWindow(holder: AdapterViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetached()
    }

    override fun onViewRecycled(holder: AdapterViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }
}

class AdapterViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private var viewMap = SparseArrayCompat<View>()

    init {
        itemView.setTag(R.id.tag_key_holder, this)
    }

    lateinit var item: AdapterItem

    fun onBind(item: AdapterItem) {
        this.item = item
        val binding = DataBindingUtil.getBinding<ViewDataBinding>(itemView)
        binding?.setVariable(item.getVariableId(), item.getItemData())
        binding?.executePendingBindings()
        if (item is WrapperAdapterItem) {
            item.executeHankers(itemView)
        }
        item.onBind(this)
    }

    fun onAttached() {
        item.onAttached(this)
    }

    fun onDetached() {
        item.onDetached(this)
    }

    fun onRecycled() {
        item.onRecycled(this)
    }

    fun onFailedToRecycleView() = item.onFailedToRecycleView(this)

    fun <T : View?> getView(id: Int) =
        viewMap.get(id) ?: run {
            itemView.findViewById<View>(id)?.also {
                viewMap.put(id, it)
            }
        } as T?
}

/**
 * never be null
 */
fun View.getAdapterHolder() =
    this.getTag(R.id.tag_key_holder) as AdapterViewHolder