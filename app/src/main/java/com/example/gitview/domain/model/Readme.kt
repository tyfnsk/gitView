package com.example.gitview.domain.model

data class Readme(
    val content: String,     // base64 çözüldükten sonra içeriği
    val encoding: String,    // genelde "base64"
    val name: String         // README dosyasının adı (README.md)
)
