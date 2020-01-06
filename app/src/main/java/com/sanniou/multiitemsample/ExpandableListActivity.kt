package com.sanniou.multiitemsample

import android.app.ProgressDialog
import com.sanniou.multiitem.AdapterItem
import com.sanniou.multiitem.DataBindingArrayList
import com.sanniou.multiitem.ItemClickAdapter
import com.sanniou.multiitemsample.model.GithubRepo

class ExpandableListActivity : BaseGithubRepoActivity() {

    override fun createAdapter(items: DataBindingArrayList<AdapterItem>) = ItemClickAdapter(items) {
        (it.item as? GithubRepo)?.run {
            if (owner.expanded) {
                items.removeAt(it.adapterPosition + 1)
            } else {
                items.add(it.adapterPosition + 1, owner)
            }
        }
    }

    override fun onViewCreated() {
        val dialog = ProgressDialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        loadData({ dialog.show() }) {
            dialog.dismiss()
            items.addAll(it)
        }
    }
}
