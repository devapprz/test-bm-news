package com.yusuf.bankmandiri.newsapps.feature.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel
@Inject
constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _state = MutableStateFlow(CategoryState())

    val state = _state.asStateFlow()

    fun findAll() = viewModelScope.launch(Dispatchers.IO) {
        categoryRepository.findAll()
            .onStart {
                _state.update { CategoryState(isLoading = true) }
            }
            .catch { error ->
                _state.update { CategoryState(messages = error.message) }
            }
            .collectLatest { collect ->
                _state.update { CategoryState(categories = collect) }
            }
    }

}