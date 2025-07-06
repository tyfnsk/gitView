package com.example.gitview.domain.repository


import com.example.gitview.domain.model.Repo

interface GitHubRepository {
    suspend fun searchRepos(query: String, page: Int): List<Repo>
    suspend fun getRepo(owner: String, repo: String): Repo
}