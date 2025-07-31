package com.example.gitview.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.usecase.FavoriteRepoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteRepoUseCase
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Repo>>(emptyList())
    val favorites: StateFlow<List<Repo>> = _favorites

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _favorites.value = favoriteUseCase.getFavorites()
            _isLoading.value = false
        }
    }

    fun addToFavorites(repo: Repo) {
        viewModelScope.launch {
            favoriteUseCase.addToFavorites(repo)
            loadFavorites() // opsiyonel: anında güncelleme
        }
    }

    fun removeFromFavorites(repo: Repo) {
        viewModelScope.launch {
            favoriteUseCase.removeFromFavorites(repo)
            loadFavorites()
        }
    }
}