package com.yusuf.bankmandiri.newsapps.utils

import java.text.SimpleDateFormat
import java.util.*

val locale = Locale("en", "EN")
fun sdfIn(pattern: String = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'") =
    SimpleDateFormat(pattern, locale).apply {
        timeZone = TimeZone.getTimeZone("GMT+00")
    }

val sdfOut = SimpleDateFormat("EEEE, dd-MMM-yyyy", locale).apply {
    timeZone = TimeZone.getDefault()
}

fun String?.toLocalDate(): String = runCatching {
    require(!isNullOrEmpty())
    try {
        val dateIn = sdfIn("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(this)
        require(dateIn != null)
        sdfOut.format(dateIn)
    } catch (e: Exception) {
        val dateIn = sdfIn().parse(this)
        require(dateIn != null)
        sdfOut.format(dateIn)
    }
}.getOrDefault(orEmpty())