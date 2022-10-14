package com.yusuf.bankmandiri.newsapps.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject
constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewsState())

    val state = _state.asStateFlow()

    fun find(search: String?, source: String) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.find(search = search, source = source, page = 1)
            .onStart {
                _state.update { NewsState(isLoading = true) }
            }
            .catch { error ->
                _state.update { NewsState(messages = error.message) }
            }
            .collectLatest { collect ->
                _state.update { NewsState(news = collect.result.orEmpty()) }
            }
    }

}