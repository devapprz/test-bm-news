package com.yusuf.bankmandiri.newsapps.feature.news

import android.content.Context
import com.github.kittinunf.fuel.httpGet
import com.yusuf.bankmandiri.newsapps.feature.news.data.News
import com.yusuf.bankmandiri.newsapps.utils.readResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NewsRemote
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {

    fun find(source: String?, page: Int, pageSize: Int) = "v2/top-headlines"
        .httpGet(
            listOfNotNull(
                source?.let { "source" to it },
                "page" to page,
                "pageSize" to pageSize
            )
        )
        .readResult<List<News>>(context = context, key = "articles")

}