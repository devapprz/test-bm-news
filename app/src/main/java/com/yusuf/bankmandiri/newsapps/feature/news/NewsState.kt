package com.yusuf.bankmandiri.newsapps.feature.news

import androidx.annotation.Keep
import com.yusuf.bankmandiri.newsapps.feature.news.data.News

@Keep
data class NewsState(
    val isLoading: Boolean = false,
    val messages: String? = null,
    val news: List<News>? = null,
)