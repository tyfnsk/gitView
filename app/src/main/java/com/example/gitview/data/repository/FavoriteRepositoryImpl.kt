package com.example.gitview.data.repository

import com.example.gitview.data.local.dao.FavoriteDao
import com.example.gitview.data.mapper.toDomain
import com.example.gitview.data.mapper.toEntity
import com.example.gitview.domain.model.Repo
import com.example.gitview.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {

    override suspend fun addFavorite(repo: Repo) {
        dao.insert(repo.toEntity())
    }

    override suspend fun removeFavorite(repo: Repo) {
        dao.delete(repo.toEntity())
    }

    override suspend fun getFavoriteRepos(): List<Repo> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun isFavorite(repo: Repo): Boolean {
        return dao.isFavorite(repo.id)
    }
}