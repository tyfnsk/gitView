package com.example.gitview.data.remote

import com.example.gitview.data.remote.dto.ReadmeDto
import com.example.gitview.data.remote.dto.RepoDto
import com.example.gitview.data.remote.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): SearchResponse

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): RepoDto

    @GET("repos/{owner}/{repo}/readme")
    suspend fun getReadme(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ReadmeDto
}