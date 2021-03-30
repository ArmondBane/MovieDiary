package com.example.moviediary.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    @Query("SELECT * FROM films")
    fun getFilmsList(): Flow<List<Film>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(film: Film)

    @Update
    suspend fun update(film: Film)

    @Delete
    suspend fun delete(film: Film)
}