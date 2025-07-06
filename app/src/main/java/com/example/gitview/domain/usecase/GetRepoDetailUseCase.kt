package com.example.gitview.domain.usecase

import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.repository.GitHubRepository
import javax.inject.Inject


class GetRepoDetailUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(owner: String, repo: String): Repo =
        repository.getRepo(owner, repo)
}