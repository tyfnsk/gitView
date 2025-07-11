package com.example.gitview.domain.usecase

import com.example.gitview.domain.repository.OpenAiRepository
import javax.inject.Inject

class SummarizeReadmeUseCase @Inject constructor(
    private val repository: OpenAiRepository
) {
    suspend operator fun invoke(content: String, repoId: String): String {
        return repository.summarize(content, repoId)
    }
}