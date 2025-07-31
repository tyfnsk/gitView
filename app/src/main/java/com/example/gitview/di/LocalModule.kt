package com.example.gitview.di

import android.content.Context
import androidx.room.Room
import com.example.gitview.data.local.dao.FavoriteDao
import com.example.gitview.data.local.dao.SummaryDao
import com.example.gitview.data.local.db.FavoriteDatabase
import com.example.gitview.data.local.db.SummaryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideSummaryDatabase(@ApplicationContext appContext: Context): SummaryDatabase {
        return Room.databaseBuilder(
            appContext,
            SummaryDatabase::class.java,
            "summary_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSummaryDao(database: SummaryDatabase): SummaryDao {
        return database.summaryDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteDatabase(@ApplicationContext context: Context): FavoriteDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            "favorite_database"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(db: FavoriteDatabase): FavoriteDao {
        return db.favoriteDao()
    }

}