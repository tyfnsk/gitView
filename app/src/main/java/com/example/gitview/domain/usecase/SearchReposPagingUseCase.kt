package com.example.gitview.domain.usecase

import androidx.paging.PagingData
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchReposPagingUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Repo>> =
        repository.searchReposPaging(query)
}