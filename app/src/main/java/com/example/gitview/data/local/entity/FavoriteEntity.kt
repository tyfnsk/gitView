package com.example.gitview.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val issues: Int,
    val language: String?,
    val htmlUrl: String,
    val ownerName: String,
    val ownerAvatarUrl: String
)
