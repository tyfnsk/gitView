package com.example.gitview.domain.model

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val issues: Int,
    val htmlUrl: String,
    val ownerName: String,
    val ownerAvatarUrl: String
)
