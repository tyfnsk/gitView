package com.example.gitview.domain.usecase

import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepoUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend fun addToFavorites(repo: Repo) {
        repository.addFavorite(repo)
    }

    suspend fun removeFromFavorites(repo: Repo) {
        repository.removeFavorite(repo)
    }

    suspend fun getFavorites(): List<Repo> {
        return repository.getFavoriteRepos()
    }

    suspend fun isFavorite(repo: Repo): Boolean {
        return repository.isFavorite(repo)
    }

}