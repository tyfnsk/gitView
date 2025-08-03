package com.example.gitview.di

import android.util.Log
import com.example.gitview.core.util.API_URL
import com.example.gitview.data.remote.GitHubApi
import com.example.gitview.data.remote.OpenAiApi
import com.example.gitview.data.repository.GitHubRepositoryImpl
import com.example.gitview.data.repository.OpenAiRepositoryImpl
import com.example.gitview.domain.repository.GitHubRepository
import com.example.gitview.domain.repository.OpenAiRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java
import com.example.gitview.BuildConfig
import com.example.gitview.data.local.dao.SummaryDao

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindGitHubRepository(
        impl: GitHubRepositoryImpl
    ): GitHubRepository
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    // ---------------- GitHub ----------------

    @Provides
    @Singleton
    @GitHubClient
    fun provideGitHubOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

    @Provides
    @Singleton
    fun provideGitHubRetrofit(
        @GitHubClient client: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideGitHubApi(retrofit: Retrofit): GitHubApi =
        retrofit.create(GitHubApi::class.java)

    // ---------------- OpenAI ----------------

    @Provides
    @Singleton
    fun provideOpenAiInterceptor(): Interceptor = Interceptor { chain ->
        Log.d("OpenAiDebug", "API Key: ${BuildConfig.OPENAI_API_KEY}")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    @OpenAiClient
    fun provideOpenAiOkHttpClient(openAiInterceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(openAiInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideOpenAiApi(
        moshi: Moshi,
        @OpenAiClient client: OkHttpClient
    ): OpenAiApi =
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(OpenAiApi::class.java)

    @Provides
    @Singleton
    fun provideOpenAiRepository(api: OpenAiApi,summaryDao: SummaryDao): OpenAiRepository =
        OpenAiRepositoryImpl(api, summaryDao)
}
