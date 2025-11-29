package com.example.pasteleriamilssaboresandroid.data.repository

import android.content.res.AssetManager
import com.example.pasteleriamilssaboresandroid.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class AssetsProductRepository(private val assets: AssetManager) : ProductRepository {
    override suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        val json = assets.open("products.json").bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        val arr: JSONArray = root.optJSONArray("products") ?: JSONArray()
        buildList(arr.length()) {
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val rawImage = o.optString("image", "").takeIf { it.isNotBlank() }
                val image = rawImage
                    ?.removePrefix("images/")
                    ?.removePrefix("/")
                    ?.takeIf { it.isNotBlank() }
                val description = o.optString("description", "").takeIf { it.isNotBlank() }
                val history = o.optString("history", "").takeIf { it.isNotBlank() }
                add(
                    Product(
                        productId = o.optString("id"),
                        productName = o.optString("name"),
                        price = o.optInt("price"),
                        category = o.optString("category"),
                        productImage = image,
                        productDescription = description,
                        popular = o.optBoolean("popular", false),
                        history = history
                    )
                )
            }
        }
    }
}
