package com.example.gitview.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "items") val items: List<RepoDto>
)

@JsonClass(generateAdapter = true)
data class RepoDto(
    val id: Long,
    val name: String,
    @Json(name = "full_name") val fullName: String,
    val description: String?,
    val language: String?,
    @Json(name = "stargazers_count") val stars: Int,
    @Json(name = "forks_count") val forks: Int,
    @Json(name = "open_issues_count") val issues: Int,
    @Json(name = "html_url") val htmlUrl: String,
    val owner: OwnerDto
)

@JsonClass(generateAdapter = true)
data class OwnerDto(
    val login: String,
    @Json(name = "avatar_url") val avatarUrl: String
)
