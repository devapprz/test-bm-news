package com.yusuf.bankmandiri.newsapps.feature.news

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NewsRepository
@Inject
constructor(
    private val newsRemote: NewsRemote
) {

    fun find(source: String?, page: Int) =
        newsRemote.find(
            source = source
                ?.lowercase()
                ?.replace(" ", "-")
                .orEmpty(),
            page = page,
            pageSize = 10
        )

}