package com.yusuf.bankmandiri.newsapps.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject
constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewsState())

    val state = _state.asStateFlow()

    fun findMock(search: String?, source: String, pageSize: Int) =
        Pager(config = PagingConfig(pageSize = pageSize))
        { newsRepository.find(search = search, source = source, pageSize = pageSize) }
            .flow
            .cachedIn(viewModelScope)

    fun showError(error: Throwable?) {
        error?.apply {
            Timber.tag("RZ_").v(this)
            _state.update { NewsState(messages = error.message) }
        }
    }

}