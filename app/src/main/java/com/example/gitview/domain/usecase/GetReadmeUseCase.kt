package com.example.gitview.domain.usecase

import com.example.gitview.domain.model.Readme
import com.example.gitview.domain.repository.GitHubRepository
import javax.inject.Inject

class GetReadmeUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(owner: String, repo: String): Readme {
        return repository.getReadme(owner, repo)
    }
}