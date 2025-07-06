package com.example.gitview.data.repository

import com.example.gitview.data.remote.GithubApi
import com.example.gitview.data.remote.dto.RepoDto
import com.example.gitview.domain.repository.GithubRepository
import javax.inject.Inject

class GitRepositoryImpl @Inject constructor(
    private val api: GithubApi
) : GithubRepository{
    override suspend fun searchRepos(query: String, page: Int): List<RepoDto> {
        return api.searchRepositories(query = query, page = page).items
    }

    override suspend fun getRepo(owner: String, repo: String): RepoDto {
        return api.getRepository(owner, repo)
    }

}