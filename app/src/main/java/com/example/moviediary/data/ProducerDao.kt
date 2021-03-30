package com.example.moviediary.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProducerDao {

    @Query("SELECT * FROM producers")
    fun getFilmsList(): Flow<List<Producer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producer: Producer)

    @Update
    suspend fun update(producer: Producer)

    @Delete
    suspend fun delete(producer: Producer)
}