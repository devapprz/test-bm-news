package com.yusuf.bankmandiri.newsapps.feature.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yusuf.bankmandiri.newsapps.feature.sources.data.Source
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.last
import javax.inject.Inject

@ViewModelScoped
class SourceRepository
@Inject constructor(
    private val sourceRemote: SourceRemote
) {

    fun findAll(category: String?, search: String?, pageSize: Int) =
        object : PagingSource<Int, Source>() {
            override fun getRefreshKey(state: PagingState<Int, Source>): Int? = null

            override suspend fun load(params: LoadParams<Int>) = params.runCatching {
                val currentPage = key ?: 1
                val result = sourceRemote.findAll(
                    category = category,
                    page = currentPage,
                    pageSize = pageSize,
                    search = search
                ).last()
                val endOfPaginationReached = result.size != pageSize
                if (result.isEmpty()) {
                    LoadResult.Page<Int, Source>(data = emptyList(), prevKey = null, nextKey = null)
                } else {
                    val prevKey = if (currentPage == 1) null else currentPage - 1
                    val nextKey = if (endOfPaginationReached) null else currentPage + 1
                    LoadResult.Page(data = result, prevKey = prevKey, nextKey = nextKey)
                }
            }.getOrElse {
                LoadResult.Error(it)
            }
        }

}