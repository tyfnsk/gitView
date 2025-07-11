package com.example.gitview.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gitview.data.local.dao.SummaryDao
import com.example.gitview.data.local.entity.SummaryEntity

@Database(
    entities = [SummaryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SummaryDatabase : RoomDatabase() {
    abstract fun summaryDao(): SummaryDao
}