package com.sanniou.multiitemsample

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sanniou.multiitemkit.vo.DividerItem
import com.sanniou.multiitemsample.databinding.MainActivityMainBinding
import kotlinx.android.synthetic.main.main_activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<MainActivityMainBinding>(
                this,
                R.layout.main_activity_main
            )

        binding.item = DividerItem(Color.BLUE, Color.RED, heightDP = 1F, paddingDP = 8F)
        toLoad.setOnClickListener {
            startActivity(Intent(this, LoadListActivity::class.java))
        }
        toExpandable.setOnClickListener {
            startActivity(Intent(this, ExpandableListActivity::class.java))
        }
    }
}
