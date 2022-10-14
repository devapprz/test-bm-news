package com.yusuf.bankmandiri.newsapps.feature.sources

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceViewModel
@Inject
constructor(
    private val sourceRepository: SourceRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SourceState())

    val state = _state.asStateFlow()

    fun findAll(category: String?) = viewModelScope.launch(Dispatchers.IO) {
        sourceRepository.findAll(category)
            .onStart {
                _state.update { SourceState(isLoading = true) }
            }
            .catch { error ->
                _state.update { SourceState(messages = error.message) }
            }
            .collectLatest { collect ->
                _state.update { SourceState(sources = collect.result.orEmpty()) }
            }
    }

    fun find(search: String?) = viewModelScope.launch(Dispatchers.IO) {
        sourceRepository.find(search = search)
            .onStart {
                _state.update { SourceState(isLoading = true) }
            }
            .catch { error ->
                _state.update { SourceState(messages = error.message) }
            }
            .collectLatest { collect ->
                _state.update { SourceState(sources = collect) }
            }
    }

}