package com.sanniou.multiitemsample

import com.sanniou.multiitemkit.vo.LoadMoreItem

class LoadListActivity : BaseGithubRepoActivity() {

    override fun onViewCreated() {
        items.add(LoadMoreItem {
            loadData { list ->
                val item = items.removeAt(items.lastIndex)
                items.addAll(list)
                items.add(item)
                it.loadSuccess(true)
            }
        })
    }
}

