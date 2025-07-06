package com.example.gitview.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.usecase.SearchReposPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val searchPaging: SearchReposPagingUseCase
) : ViewModel() {
    private var currentQuery: String = "android"
    val repos: Flow<PagingData<Repo>> = searchPaging(currentQuery).cachedIn(viewModelScope)
    fun search(query: String) { currentQuery = query }
}