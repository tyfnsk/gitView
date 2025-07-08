package com.example.gitview.data.mapper

import android.util.Base64
import com.example.gitview.data.remote.dto.ReadmeDto
import com.example.gitview.domain.model.Readme
import java.nio.charset.StandardCharsets

fun ReadmeDto.toDomain(): Readme {
    val decoded = try {
        val decodedBytes = Base64.decode(content ?: "", Base64.DEFAULT)
        String(decodedBytes, StandardCharsets.UTF_8)
    } catch (e: Exception) {
        "(README çözümlenemedi)"
    }

    return Readme(
        content = decoded,
        encoding = encoding ?: "unknown",
        name = name ?: "README"
    )
}