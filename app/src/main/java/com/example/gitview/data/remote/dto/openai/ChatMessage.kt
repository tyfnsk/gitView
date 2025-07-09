package com.example.gitview.data.remote.dto.openai

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatMessage(
    val role: String,   // "user", "assistant", "system"
    val content: String
)
