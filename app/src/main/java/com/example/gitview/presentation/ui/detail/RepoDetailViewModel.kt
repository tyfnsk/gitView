package com.example.gitview.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.usecase.FavoriteRepoUseCase
import com.example.gitview.domain.usecase.GetReadmeUseCase
import com.example.gitview.domain.usecase.GetRepoDetailUseCase
import com.example.gitview.domain.usecase.SummarizeReadmeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    private val getRepoDetail: GetRepoDetailUseCase,
    private val getReadme: GetReadmeUseCase,
    private val summarizeReadme: SummarizeReadmeUseCase,
    private val favoriteUseCase: FavoriteRepoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun load(owner: String, repo: String) {
        _uiState.value = DetailUiState.Loading
        viewModelScope.launch {
            try {
                val repoData = getRepoDetail(owner, repo)
                val readme = getReadme(owner, repo)
                val combined = repoData.copy(readme = readme)

                _uiState.value = DetailUiState.Success(
                    repo = combined,
                    summary = null,
                    isSummaryLoading = false
                )

                _isFavorite.value = favoriteUseCase.isFavorite(combined)

            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(
                    e.localizedMessage ?: "Beklenmeyen hata"
                )
            }
        }
    }

    fun summarizeReadmeIfNeeded() {
        val currentState = _uiState.value
        if (currentState is DetailUiState.Success) {
            if (currentState.summary != null || currentState.isSummaryLoading) return

            val readmeContent = currentState.repo.readme.content
            val repoId = currentState.repo.fullName
            if (readmeContent.isBlank()) return

            _uiState.update {
                currentState.copy(isSummaryLoading = true)
            }

            viewModelScope.launch {
                try {
                    val summary = summarizeReadme(readmeContent, repoId)
                    _uiState.update {
                        currentState.copy(summary = summary, isSummaryLoading = false)
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        currentState.copy(isSummaryLoading = false)
                    }
                }
            }
        }
    }

    fun addToFavorites(repo: Repo) {
        viewModelScope.launch {
            favoriteUseCase.addToFavorites(repo)
            _isFavorite.value = true
        }
    }

    fun removeFromFavorites(repo: Repo) {
        viewModelScope.launch {
            favoriteUseCase.removeFromFavorites(repo)
            _isFavorite.value = false
        }
    }
}

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Error(val message: String) : DetailUiState
    data class Success(
        val repo: Repo,
        val summary: String? = null,
        val isSummaryLoading: Boolean = false
    ) : DetailUiState
}