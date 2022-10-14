package com.yusuf.bankmandiri.newsapps.feature.news.data

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.yusuf.bankmandiri.newsapps.feature.sources.data.Source

@Keep
data class News(
    @SerializedName("source")
    @Expose
    val source: Source? = null,
    @SerializedName("author")
    @Expose
    val author: String? = null,
    @SerializedName("title")
    @Expose
    val title: String? = null,
    @SerializedName("description")
    @Expose
    val description: String? = null,
    @SerializedName("url")
    @Expose
    val url: String? = null,
    @SerializedName("urlToImage")
    @Expose
    val urlToImage: String? = null,
    @SerializedName("publishedAt")
    @Expose
    val publishedAt: String? = null,
    @SerializedName("content")
    @Expose
    val content: String? = null
)