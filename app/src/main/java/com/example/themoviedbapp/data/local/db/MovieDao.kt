package com.example.themoviedbapp.data.local.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.themoviedbapp.models.entity.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getMoviesPaged(): PagingSource<Int, Movie>

    @Query("SELECT COUNT(id) FROM movies")
    suspend fun getMoviesCount(): Int

    @Query("SELECT * FROM movies WHERE id=:id")
    suspend fun getMovieById(id: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}