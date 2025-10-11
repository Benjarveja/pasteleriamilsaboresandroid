package com.example.pasteleriamilssaboresandroid.data.repository

import android.content.res.AssetManager
import com.example.pasteleriamilssaboresandroid.domain.model.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class AssetsNewsRepository(private val assets: AssetManager) {
    suspend fun getNews(): List<NewsItem> = withContext(Dispatchers.IO) {
        val json = assets.open("news.json").bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        val arr: JSONArray = root.optJSONArray("news") ?: JSONArray()
        buildList(arr.length()) {
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                add(
                    NewsItem(
                        id = o.optString("id"),
                        title = o.optString("title"),
                        excerpt = o.optString("excerpt"),
                        image = o.optString("image", "").takeIf { it.isNotBlank() },
                        href = o.optString("href", "").takeIf { it.isNotBlank() }
                    )
                )
            }
        }
    }
}

