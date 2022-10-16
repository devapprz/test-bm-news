package com.yusuf.bankmandiri.newsapps.feature.sources

import android.content.Context
import com.github.kittinunf.fuel.httpGet
import com.yusuf.bankmandiri.newsapps.feature.sources.data.Source
import com.yusuf.bankmandiri.newsapps.utils.readResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

@ViewModelScoped
class SourceRemote
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {

    private val _temp = mutableListOf<Source>()
    private var mFirstData = 0
    private var mLastData = 0

    fun findAll(category: String?, search: String?, page: Int, pageSize: Int) = flow {
        if (!search.isNullOrEmpty() || page == 0) {
            mFirstData = 0
            mLastData = 0
        }
        if (page == 0) {
            val result = "v2/top-headlines/sources"
                .httpGet(listOf("category" to (category.orEmpty().lowercase())))
                .readResult<List<Source>>(context = context, key = "sources")
                .catch { throw it }
                .last()
            _temp.clear()
            _temp.addAll(result.orEmpty())
        } else {
            kotlinx.coroutines.delay(1000)
        }
        mFirstData += (if (page == 0) 0 else pageSize)
        mLastData += (if (page == 0) (pageSize - 1) else pageSize)
        val result = _temp.filter {
            if (search.isNullOrEmpty()) {
                true
            } else {
                it.name // find by name
                    .orEmpty() // if data is null set as empty string
                    .lowercase() // avoid upper case
                    .contains(search.lowercase()) // find data by close text
            }
        }.runCatching {
            require(isNotEmpty())
            slice(mFirstData..mLastData)
        }.getOrDefault(emptyList())
        emit(result)
    }

}