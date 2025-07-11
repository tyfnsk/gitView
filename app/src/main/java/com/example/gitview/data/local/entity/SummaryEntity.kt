package com.example.gitview.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summaries")
data class SummaryEntity(
    @PrimaryKey val repoFullName: String, // örn. "octocat/Hello-World"
    val summary: String
)
