package com.yusuf.bankmandiri.newsapps.utils

import androidx.annotation.Keep

@Keep
data class ResponseWrapper<T>(
    val status: String,
    val message: String?,
    val result: T?
)