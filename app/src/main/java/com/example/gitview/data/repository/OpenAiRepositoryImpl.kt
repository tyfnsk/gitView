package com.example.gitview.data.repository

import com.example.gitview.core.util.cleanReadme
import com.example.gitview.data.local.dao.SummaryDao
import com.example.gitview.data.local.entity.SummaryEntity
import com.example.gitview.data.remote.OpenAiApi
import com.example.gitview.data.remote.dto.openai.ChatMessage
import com.example.gitview.data.remote.dto.openai.ChatRequest
import com.example.gitview.domain.repository.OpenAiRepository
import javax.inject.Inject

class OpenAiRepositoryImpl @Inject constructor(
    private val api: OpenAiApi,
    private val summaryDao: SummaryDao
) : OpenAiRepository {

    override suspend fun summarize(text: String, repoId: String): String {
        val cleanedContent = cleanReadme(text)

        // looking first cache
        val cached = summaryDao.getSummaryByRepoFullName(repoId)
        if (cached != null) {
            return cached.summary
        }

        // Calling API
        val request = ChatRequest(
            messages = listOf(
                ChatMessage(
                    role = "system",
                    content = "You are a helpful assistant that summarizes GitHub README files."
                ),
                ChatMessage(
                    role = "user",
                    content = """
                        Summarize the following README content in 2-3 concise, technical English sentences.
                        Focus only on what the project does, what technologies it uses, and its key features.

                        $cleanedContent
                    """.trimIndent()
                )
            )
        )

        val response = api.summarizeReadme(request)
        val summary = response.choices.firstOrNull()?.message?.content?.trim()
            ?: "No summary available"


        // Writing on cache
        summaryDao.insertSummary(SummaryEntity(repoFullName = repoId, summary = summary))

        return summary
    }
}