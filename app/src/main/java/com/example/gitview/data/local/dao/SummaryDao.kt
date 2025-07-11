package com.example.gitview.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitview.data.local.entity.SummaryEntity

@Dao
interface SummaryDao {

    @Query("SELECT * FROM summaries WHERE repoFullName = :fullName LIMIT 1")
    suspend fun getSummaryByRepoFullName(fullName: String): SummaryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: SummaryEntity)
}