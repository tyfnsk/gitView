package com.example.gitview.data.repository

import android.util.Log
import com.example.gitview.core.util.cleanReadme
import com.example.gitview.data.remote.OpenAiApi
import com.example.gitview.data.remote.dto.openai.ChatMessage
import com.example.gitview.data.remote.dto.openai.ChatRequest
import com.example.gitview.domain.repository.OpenAiRepository
import javax.inject.Inject

class OpenAiRepositoryImpl @Inject constructor(
    private val api: OpenAiApi
) : OpenAiRepository {

    override suspend fun summarize(text: String): String {
        val cleanedContent = cleanReadme(text)
        Log.d("OpenAiTrace", "summarizeReadme() API çağrılacak")
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
        Log.d("OpenAiTrace", "UseCase başlıyor")
        val response = api.summarizeReadme(request)
        Log.d("OpenAiTrace", "Repository'den response geldi")
        Log.d("OpenAiDebugResponse", "choices: ${response.choices}")
        Log.d("OpenAiDebugResponse", "first content: ${response.choices.firstOrNull()?.message?.content}")
        return response.choices.firstOrNull()?.message?.content?.trim()
            ?: "No summary available"
    }
}