package com.example.gitview.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gitview.data.local.dao.FavoriteDao
import com.example.gitview.data.local.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}