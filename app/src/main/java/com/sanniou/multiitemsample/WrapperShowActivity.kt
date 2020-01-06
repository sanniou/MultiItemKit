package com.sanniou.multiitemsample

import android.app.ProgressDialog
import android.graphics.Color
import com.sanniou.multiitemsample.vo.MultiWrapperItem

class WrapperShowActivity : BaseGithubRepoActivity() {

    override fun onViewCreated() {
        val dialog = ProgressDialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        loadData({ dialog.show() }) { list ->
            dialog.dismiss()
            list.forEachIndexed { index, githubRepo ->
                items.add(
                    when (index.rem(10)) {
                        0 -> MultiWrapperItem(githubRepo).apply {
                            roundHandler.roundColor = Color.GREEN
                        }
                        1 -> MultiWrapperItem(githubRepo).apply {
                            marginHandler.margin(18)
                        }
                        2 -> MultiWrapperItem(githubRepo).apply {
                            paddingHandler.padding(28)
                        }
                        3 -> MultiWrapperItem(githubRepo).apply {
                            roundHandler.roundColor = Color.WHITE
                        }
                        4 -> MultiWrapperItem(githubRepo).apply {
                            roundHandler.roundDrawable = R.mipmap.ic_launcher
                        }
                        // 不能直接返回 githubRepo 原始类型，因为布局都是一样的，View会被复用
                        // MultiWrapperItem 会清除之前的属性
                        else -> MultiWrapperItem(githubRepo)
                    }
                )
            }
        }
    }
}
