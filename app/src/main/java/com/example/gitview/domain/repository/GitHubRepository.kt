package com.example.gitview.domain.repository


import androidx.paging.PagingData
import com.example.gitview.domain.model.Readme
import com.example.gitview.domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    suspend fun searchRepos(query: String, page: Int): List<Repo>
    suspend fun getRepo(owner: String, repo: String): Repo

    suspend fun getReadme(owner: String, repo: String): Readme

    fun searchReposPaging(query: String): Flow<PagingData<Repo>>
}