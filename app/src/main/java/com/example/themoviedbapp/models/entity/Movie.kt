package com.example.themoviedbapp.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val idDb: Long = 0L,
    val id: Int,
    val releaseDate: String?,
    val title: String,
    val overview: String?,
    val posterUrl: String?,
)