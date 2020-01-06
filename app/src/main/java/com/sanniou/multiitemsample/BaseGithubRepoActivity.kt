package com.sanniou.multiitemsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sanniou.multiitem.AdapterItem
import com.sanniou.multiitem.DataBindingArrayList
import com.sanniou.multiitem.MultiItemAdapter
import com.sanniou.multiitemsample.model.GithubRepo
import com.sanniou.multiitemsample.model.toGithubRepos
import com.sanniou.multiitemsample.utils.toRequestGet
import kotlinx.android.synthetic.main.main_activity_load_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val API_REPOSITORIES = "https://api.github.com/repositories"

abstract class BaseGithubRepoActivity : AppCompatActivity() {

    val items = DataBindingArrayList<AdapterItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_load_list)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = createAdapter(items)

        onViewCreated()
    }

    open fun createAdapter(items: DataBindingArrayList<AdapterItem>) = MultiItemAdapter(items)

    abstract fun onViewCreated()

    protected fun loadData(
        startCallback: () -> Unit = {},
        endCallback: (items: List<GithubRepo>) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            startCallback()
            val repos = API_REPOSITORIES.toRequestGet().toGithubRepos()
            delay(1000L)
            endCallback(repos)
        }
    }
}
