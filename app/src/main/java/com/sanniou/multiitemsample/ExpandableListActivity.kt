package com.sanniou.multiitemsample

import android.app.ProgressDialog
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitem.ItemClickAdapter
import com.sanniou.multiitem.MultiItemArrayList
import com.sanniou.multiitem.OnItemClickListener
import com.sanniou.multiitemsample.model.GithubRepo

class ExpandableListActivity : BaseGithubRepoActivity() {

    override fun createAdapter(items: MultiItemArrayList<DataItem>) = ItemClickAdapter(items,
        OnItemClickListener {
            (it.item as? GithubRepo)?.run {
                if (owner.expanded) {
                    items.removeAt(it.adapterPosition + 1)
                } else {
                    items.add(it.adapterPosition + 1, owner)
                }
            }
        })

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
