package com.example.gitview.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReadmeDto(
    @Json(name = "content") val content: String?,
    @Json(name = "encoding") val encoding: String?,
    @Json(name = "name") val name: String?
)
