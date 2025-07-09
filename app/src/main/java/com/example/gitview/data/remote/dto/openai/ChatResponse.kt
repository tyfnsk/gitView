package com.example.gitview.data.remote.dto.openai

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val choices: List<Choice>
)
@JsonClass(generateAdapter = true)
data class Choice(
    val message: ChatMessage
)
