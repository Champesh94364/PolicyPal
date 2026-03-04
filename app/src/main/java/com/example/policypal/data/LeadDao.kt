package com.example.policypal.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LeadDao {
    @Query("SELECT * FROM leads ORDER BY id DESC")
    fun observeAll(): Flow<List<LeadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lead: LeadEntity)

    @Update
    suspend fun update(lead: LeadEntity)

    @Delete
    suspend fun delete(lead: LeadEntity)
}