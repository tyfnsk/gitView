package com.example.gitview.core.util

import com.example.gitview.R

fun getLanguageIcon(language: String): Int {
    return when (language.lowercase()) {
        "kotlin" -> R.drawable.kotlin
        "java" -> R.drawable.java
        "javascript" -> R.drawable.javascript
        "typescript" -> R.drawable.typescript
        "html" -> R.drawable.html
        "css" -> R.drawable.css3
        "c#" -> R.drawable.c_sharp
        "go" -> R.drawable.go
        "rust" -> R.drawable.rust
        "php" -> R.drawable.php
        "python" -> R.drawable.python
        "c++" -> R.drawable.cplus
        "c" -> R.drawable.c
        "docker" -> R.drawable.docker
        "swift" -> R.drawable.swift
        "objective-c" -> R.drawable.objective_c
        else -> R.drawable.ic_launcher_foreground // yedek ikon
    }
}