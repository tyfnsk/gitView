package com.example.gitview.data.mapper

import com.example.gitview.data.remote.dto.RepoDto
import com.example.gitview.domain.model.Repo

fun RepoDto.toDomain(): Repo = Repo(
    id = id,
    name = name,
    fullName = fullName,
    description = description,
    language = language,
    stars = stars,
    forks = forks,
    issues = issues,
    htmlUrl = htmlUrl,
    ownerName = owner.login,
    ownerAvatarUrl = owner.avatarUrl
)