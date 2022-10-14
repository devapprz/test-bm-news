package com.yusuf.bankmandiri.newsapps.feature.sources

import androidx.annotation.Keep
import com.yusuf.bankmandiri.newsapps.feature.sources.data.Source

@Keep
data class SourceState(
    val isLoading: Boolean = false,
    val messages: String? = null,
    val sources: List<Source>? = null,
)