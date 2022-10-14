package com.yusuf.bankmandiri.newsapps.feature.sources

import android.content.Context
import com.github.kittinunf.fuel.httpGet
import com.yusuf.bankmandiri.newsapps.feature.sources.data.Source
import com.yusuf.bankmandiri.newsapps.utils.readResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SourceRemote
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {

    fun findAll(category: String?) = "v2/top-headlines/sources"
        .httpGet(listOf("category" to (category?.lowercase().orEmpty())))
        .readResult<List<Source>>(context = context, key = "sources")

}