package com.example.gitview.domain.repository

import com.example.gitview.domain.model.Repo

interface FavoriteRepository {
    suspend fun addFavorite(repo: Repo)
    suspend fun removeFavorite(repo: Repo)
    suspend fun getFavoriteRepos(): List<Repo>
    suspend fun isFavorite(repo: Repo): Boolean
}