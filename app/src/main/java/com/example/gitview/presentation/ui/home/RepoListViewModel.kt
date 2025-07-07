package com.example.gitview.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.usecase.SearchReposPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val searchPaging: SearchReposPagingUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("android")
    val query: StateFlow<String> = _query.asStateFlow()

    val repos: Flow<PagingData<Repo>> = _query
        .debounce(300) // Küçük gecikme, hızlı yazarken istekleri azaltır
        .distinctUntilChanged()
        .flatMapLatest { q ->
            searchPaging(q).cachedIn(viewModelScope)
        }

    fun search(newQuery: String) {
        _query.value = newQuery
    }
}