package com.example.gitview.data.repository

import com.example.gitview.data.mapper.toDomain
import com.example.gitview.data.remote.GitHubApi
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val api: GitHubApi
) : GitHubRepository {
    override suspend fun searchRepos(query: String, page: Int): List<Repo> {
        return api.searchRepositories(query = query, page = page).items.map { it.toDomain() }
    }

    override suspend fun getRepo(owner: String, repo: String): Repo {
        return api.getRepository(owner, repo).toDomain()
    }
}