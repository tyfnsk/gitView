package com.example.gitview.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GitHubClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenAiClient
