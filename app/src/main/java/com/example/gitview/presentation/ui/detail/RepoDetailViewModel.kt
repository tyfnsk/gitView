package com.example.gitview.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.usecase.GetRepoDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    private val getRepoDetail: GetRepoDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun load(owner: String, repo: String) {
        _uiState.value = DetailUiState.Loading
        viewModelScope.launch {
            _uiState.value = try {
                val result = getRepoDetail(owner, repo)
                DetailUiState.Success(result)
            } catch (e: Exception) {
                DetailUiState.Error(e.localizedMessage ?: "Beklenmeyen hata")
            }
        }
    }
}

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Error(val message: String) : DetailUiState
    data class Success(val repo: Repo) : DetailUiState
}