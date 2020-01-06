package com.sanniou.multiitemsample.model

import com.sanniou.multiitem.AdapterViewHolder
import com.sanniou.multiitem.DataItem
import com.sanniou.multiitemsample.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

suspend fun String.toGithubRepos() = withContext(Dispatchers.IO) {
    val list = mutableListOf<GithubRepo>()
    val jsonArray = JSONArray(this@toGithubRepos)
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        list.add(jsonObject.toGithubRepo())
    }
    list
}

fun JSONObject.toGithubRepo() = GithubRepo(
    getString("archive_url"),
    getString("branches_url"),
    getString("full_name"),
    getString("html_url"),
    getInt("id"),
    getString("name"),
    getJSONObject("owner").toOwner(),
    getString("url")
)

fun JSONObject.toOwner() = Owner(
    getString("avatar_url"),
    getString("html_url"),
    getInt("id"),
    getString("login"),
    getString("type"),
    getString("url")
)

data class GithubRepo(
    val archive_url: String,
    val branches_url: String,
    val full_name: String,
    val html_url: String,
    val id: Int,
    val name: String,
    val owner: Owner,
    val url: String
) : DataItem {
    override fun getItemType() = R.layout.main_item_github_repo
}

data class Owner(
    val avatar_url: String,
    val html_url: String,
    val id: Int,
    val login: String,
    val type: String,
    val url: String
    ) : DataItem {

    var expanded: Boolean = false

    override fun onAttached(holder: AdapterViewHolder) {
        expanded = true
    }

    override fun onDetached(holder: AdapterViewHolder) {
        expanded = false
    }

    override fun getItemType() = R.layout.main_item_github_owner
}
