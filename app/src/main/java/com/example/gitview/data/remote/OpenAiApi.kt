package com.example.gitview.data.remote

import com.example.gitview.data.remote.dto.openai.ChatRequest
import com.example.gitview.data.remote.dto.openai.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {
    @Headers(
        "Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun summarizeReadme(@Body request: ChatRequest): ChatResponse
}