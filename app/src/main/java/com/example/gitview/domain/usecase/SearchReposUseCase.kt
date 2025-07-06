package com.example.gitview.domain.usecase

import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.repository.GitHubRepository
import javax.inject.Inject

class SearchReposUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    /**
     * @param query   Full‑text GitHub search string (e.g. "language:kotlin stars:>500")
     * @param page    Pagination page (1‑based)
     */
    suspend operator fun invoke(query: String, page: Int = 1): List<Repo> =
        repository.searchRepos(query, page)
}