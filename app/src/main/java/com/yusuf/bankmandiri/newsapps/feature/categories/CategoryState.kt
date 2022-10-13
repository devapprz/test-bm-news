package com.yusuf.bankmandiri.newsapps.feature.categories

import androidx.annotation.Keep
import com.yusuf.bankmandiri.newsapps.feature.categories.data.Category

@Keep
data class CategoryState(
    val categories: List<Category>? = null,
    val isLoading: Boolean = false,
    val messages: String? = null,
)