package com.yusuf.bankmandiri.newsapps.feature.sources

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
class SourceViewModel
@Inject
constructor(
    private val sourceRepository: SourceRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SourceState())

    val state = _state.asStateFlow()

    fun findAll(category: String?, search: String?) =
        Pager(config = PagingConfig(pageSize = 4))
        { sourceRepository.findAll(category, search = search, pageSize = 12) }
            .flow
            .cachedIn(viewModelScope)

    fun showError(error: Throwable?) {
        error?.apply {
            Timber.tag("RZ_").v(this)
            _state.update { SourceState(messages = error.message) }
        }
    }

}