package com.yusuf.bankmandiri.newsapps.feature.news

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NewsRepository
@Inject
constructor(
    private val newsRemote: NewsRemote
) {

    fun find(search: String?, source: String, page: Int) =
        newsRemote.find(
            search = search,
            source = source
                .lowercase()
                .replace(" ", "-"),
            page = page,
            pageSize = 10
        )

}