package com.example.gitview.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitview.data.mapper.toDomain
import com.example.gitview.data.remote.GitHubApi
import com.example.gitview.domain.model.Repo

class RepoPagingSource(
    private val api: GitHubApi,
    private val query: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val page = params.key ?: 1
            val response = api.searchRepositories(query = query, page = page)
            val repos = response.items.map { it.toDomain() }
            val nextKey = if (repos.isEmpty()) null else page + 1
            LoadResult.Page(
                data = repos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}