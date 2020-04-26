package com.sanniou.multiitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class MultiItemAdapter<T : DataItem>(
    items: MultiItemArrayList<T>
) : RecyclerView.Adapter<AdapterViewHolder>() {

    private var mCallback = AdapterItemsChangedCallback<T>(this)

    var data: MultiItemArrayList<T> = items
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

    fun getItem(i: Int): T {
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

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
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
        itemView.setTag(R.id.multiitem_tag_key_holder, this)
    }

    lateinit var item: DataItem

    fun onBind(item: DataItem) {
        this.item = item
        val binding = DataBindingUtil.getBinding<ViewDataBinding>(itemView)
        binding?.run {
            setVariable(item.getVariableId(), item.getItemData())
            executePendingBindings()
        }
        item.onBind(this)
    }

    fun rebind() {
        onBind(item)
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
    this.getTag(R.id.multiitem_tag_key_holder) as AdapterViewHolder