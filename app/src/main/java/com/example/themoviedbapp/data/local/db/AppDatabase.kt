package com.example.themoviedbapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.themoviedbapp.models.entity.Movie
import com.example.themoviedbapp.models.entity.MovieRemoteKeys

@Database(entities = [Movie::class, MovieRemoteKeys::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao
}