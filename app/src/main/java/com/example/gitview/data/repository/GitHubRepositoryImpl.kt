package com.example.gitview.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gitview.data.mapper.toDomain
import com.example.gitview.data.paging.RepoPagingSource
import com.example.gitview.data.remote.GitHubApi
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val api: GitHubApi
) : GitHubRepository {

    // non‑paged
    override suspend fun searchRepos(query: String, page: Int): List<Repo> =
        api.searchRepositories(query = query, page = page).items.map { it.toDomain() }

    override suspend fun getRepo(owner: String, repo: String): Repo =
        api.getRepository(owner, repo).toDomain()

    // paged flow
    override fun searchReposPaging(query: String): Flow<PagingData<Repo>> =
        Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { RepoPagingSource(api, query) }
        ).flow
}