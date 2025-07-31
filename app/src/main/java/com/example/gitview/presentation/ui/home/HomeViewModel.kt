package com.example.gitview.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.usecase.FavoriteRepoUseCase
import com.example.gitview.domain.usecase.SearchReposPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteRepoUseCase,
    private val searchPaging: SearchReposPagingUseCase
) : ViewModel() {

    private val _favoriteRepos = MutableStateFlow<List<Repo>>(emptyList())
    val favoriteRepos: StateFlow<List<Repo>> = _favoriteRepos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadFavorites()
    }

    private val _query = MutableStateFlow("android")
    val query: StateFlow<String> = _query.asStateFlow()
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val repos: Flow<PagingData<Repo>> = _query
        .debounce(300) // Küçük gecikme, hızlı yazarken istekleri azaltır
        .distinctUntilChanged()
        .flatMapLatest { q ->
            searchPaging(q).cachedIn(viewModelScope)
        }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _favoriteRepos.value = favoriteUseCase.getFavorites()
            _isLoading.value = false
        }
    }

    fun addToFavorites(repo: Repo) {
        viewModelScope.launch {
            favoriteUseCase.addToFavorites(repo)
            loadFavorites()
        }
    }

    fun removeFromFavorites(repo: Repo) {
        viewModelScope.launch {
            favoriteUseCase.removeFromFavorites(repo)
            loadFavorites()
        }
    }
}