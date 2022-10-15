package com.yusuf.bankmandiri.newsapps.feature.news

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yusuf.bankmandiri.newsapps.feature.news.data.News
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.last
import javax.inject.Inject

@ViewModelScoped
class NewsRepository
@Inject
constructor(
    private val newsRemote: NewsRemote
) {

    fun findAll(search: String?, source: String, pageSize: Int) = object : PagingSource<Int, News>() {
        override fun getRefreshKey(state: PagingState<Int, News>): Int? = null

        @Suppress("RemoveExplicitTypeArguments")
        override suspend fun load(params: LoadParams<Int>) = params.runCatching {
            val currentPage = key ?: 1
            val result = newsRemote.find(
                search = search,
                source = source
                    .lowercase()
                    .replace(" ", "-"),
                page = currentPage,
                pageSize = pageSize
            ).last()
            val resultData = result.orEmpty()
            val endOfPaginationReached = resultData.size != pageSize
            if (resultData.isEmpty()) {
                LoadResult.Page<Int, News>(data = emptyList(), prevKey = null, nextKey = null)
            } else {
                val prevKey = if (currentPage == 1) null else currentPage - 1
                val nextKey = if (endOfPaginationReached) null else currentPage + 1
                LoadResult.Page(data = resultData, prevKey = prevKey, nextKey = nextKey)
            }
        }.getOrElse {
            LoadResult.Error<Int, News>(it)
        }
    }

}