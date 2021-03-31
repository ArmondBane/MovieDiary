package com.example.moviediary.data

import android.provider.ContactsContract
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    fun getFilmsList(searchQuery: String, sortOrder: SortOrder): Flow<List<Film>> =
            when(sortOrder) {
                SortOrder.BY_NAME -> getFilmsListByName(searchQuery)
                SortOrder.BY_YEAR -> getFilmsListByYear(searchQuery)
                SortOrder.BY_GENRE -> getFilmsListByGenre(searchQuery)
                SortOrder.BY_PRODUCER -> getFilmsListByProducer(searchQuery)
            }

    @Query("SELECT * FROM films WHERE name LIKE '%' || :searchQuery || '%'")
    fun getFilmsListByName(searchQuery: String): Flow<List<Film>>

    @Query("SELECT * FROM films WHERE strftime('%Y', year_of_issue) LIKE '%' || :searchQuery || '%'")
    fun getFilmsListByYear(searchQuery: String): Flow<List<Film>>

    @Query("SELECT * FROM films WHERE genre LIKE '%' || :searchQuery || '%'")
    fun getFilmsListByGenre(searchQuery: String): Flow<List<Film>>

    @Query("SELECT * FROM films WHERE id IN (SELECT film_id FROM producers WHERE name LIKE '%' || :searchQuery || '%' )")
    fun getFilmsListByProducer(searchQuery: String): Flow<List<Film>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(film: Film)

    @Update
    suspend fun update(film: Film)

    @Delete
    suspend fun delete(film: Film)
}