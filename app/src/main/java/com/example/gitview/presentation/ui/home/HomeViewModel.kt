package com.example.gitview.presentation.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitview.R
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.usecase.FavoriteRepoUseCase
import com.example.gitview.domain.usecase.SearchReposPagingUseCase
import com.example.gitview.presentation.ui.category.CategoryItem
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

    private val _categories = MutableStateFlow<List<CategoryItem>>(emptyList())
    val categories: StateFlow<List<CategoryItem>> = _categories

    private val _query = MutableStateFlow("android")
    val query: StateFlow<String> = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val repos: Flow<PagingData<Repo>> = _query
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { q ->
            searchPaging(q).cachedIn(viewModelScope)
        }

    init {
        loadFavorites()
        loadCategories()
    }

    private fun loadCategories() {
        _categories.value = listOf(
            CategoryItem(
                name = "Android",
                languages = listOf("kotlin", "java"),
                iconRes = R.drawable.android_logo, // kendi ikonlarını eklemelisin
                repoCount = 0
            ),
            CategoryItem(
                name = "iOS",
                languages = listOf("swift", "objective-c"),
                iconRes = R.drawable.ios_logo,
                repoCount = 0
            ),
            CategoryItem(
                name = "Web",
                languages = listOf("javascript", "typescript", "html", "css"),
                iconRes = R.drawable.web_logo,
                repoCount = 0
            ),
            CategoryItem(
                name = "Backend",
                languages = listOf("c#", "go", "rust", "php"),
                iconRes = R.drawable.backend_logo,
                repoCount = 0
            ),
            CategoryItem(
                name = "AI/ML",
                languages = listOf("python"),
                iconRes = R.drawable.ai_logo,
                repoCount = 0
            ),
            CategoryItem(
                name = "DevOps",
                languages = listOf("docker"),
                iconRes = R.drawable.devops_logo,
                repoCount = 0
            ),
            CategoryItem(
                name = "System",
                languages = listOf("c++", "c"),
                iconRes = R.drawable.system_logo,
                repoCount = 0
            ),
            CategoryItem(
                name = "Others",
                languages = emptyList(),
                iconRes = R.drawable.git_logo,
                repoCount = 0
            )
        )
    }

    fun setQuery(newQuery: String) {
        _query.value = newQuery
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