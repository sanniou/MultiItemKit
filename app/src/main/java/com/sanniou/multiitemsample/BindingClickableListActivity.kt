package com.sanniou.multiitemsample

import android.app.ProgressDialog
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import com.sanniou.multiitemsample.databinding.ActivityBindingListBinding
import com.sanniou.multiitemsample.model.GithubRepo

class BindingClickableListActivity : BaseGithubRepoActivity() {

    override fun initView() {
        val binding =
            DataBindingUtil.setContentView<ActivityBindingListBinding>(
                this,
                R.layout.activity_binding_list
            )
        binding.data = items
        binding.setItemClickListener {
            Toast.makeText(this, (it.item as GithubRepo).full_name, LENGTH_SHORT).show()
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
