package com.example.gitview.domain.repository

import com.example.gitview.data.remote.dto.RepoDto

interface GithubRepository {
    suspend fun searchRepos(query: String, page: Int): List<RepoDto>
    suspend fun getRepo(owner: String, repo: String): RepoDto
}