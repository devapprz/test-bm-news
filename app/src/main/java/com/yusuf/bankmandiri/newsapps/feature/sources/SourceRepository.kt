package com.yusuf.bankmandiri.newsapps.feature.sources

import com.yusuf.bankmandiri.newsapps.feature.sources.data.Source
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ViewModelScoped
class SourceRepository
@Inject
constructor(
    private val sourceRemote: SourceRemote
) {

    private val _sourceCache = mutableListOf<Source>()

    fun findAll(category: String?) =
        sourceRemote.findAll(category).onEach {
            if (!it.message.isNullOrEmpty()) {
                _sourceCache.clear()
                _sourceCache.addAll(it.result.orEmpty())
            }
        }

    fun find(search: String?) = flow {
        kotlinx.coroutines.delay(1500)
        val result = if (search.isNullOrEmpty()) {
            _sourceCache
        } else {
            _sourceCache.filter {
                it.name?.contains(search) == true
            }
        }
        emit(result)
    }

}