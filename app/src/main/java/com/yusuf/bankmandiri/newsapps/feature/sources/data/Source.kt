package com.yusuf.bankmandiri.newsapps.feature.sources.data

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Source(
    @SerializedName("id")
    @Expose
    val id: String? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("description")
    @Expose
    val description: String? = null,
    @SerializedName("url")
    @Expose
    val url: String? = null,
    @SerializedName("category")
    @Expose
    val category: String? = null,
    @SerializedName("language")
    @Expose
    val language: String? = null,
    @SerializedName("country")
    @Expose
    val country: String? = null
)