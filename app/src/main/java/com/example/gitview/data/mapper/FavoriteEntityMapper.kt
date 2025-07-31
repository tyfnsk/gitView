package com.example.gitview.data.mapper

import com.example.gitview.data.local.entity.FavoriteEntity
import com.example.gitview.domain.model.Repo

fun FavoriteEntity.toDomain(): Repo {
    return Repo(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        stars = stars,
        forks = forks,
        issues = issues,
        language = language,
        htmlUrl = htmlUrl,
        ownerName = ownerName,
        ownerAvatarUrl = ownerAvatarUrl
    )
}

fun Repo.toEntity(): FavoriteEntity {
    return FavoriteEntity(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        stars = stars,
        forks = forks,
        issues = issues,
        language = language,
        htmlUrl = htmlUrl,
        ownerName = ownerName,
        ownerAvatarUrl = ownerAvatarUrl
    )
}