package com.example.gitview.presentation.ui.category

data class CategoryItem(
    val name: String,
    val languages: List<String>,
    val iconRes: Int,
    val repoCount: Int
)