package com.example.gitview.domain.repository

interface OpenAiRepository {
    suspend fun summarize(text: String, repoId: String): String
}