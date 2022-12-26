package com.example.themoviedbapp.models.entity

import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(primaryKeys = [("id")], tableName = "movies")
data class Movie(
    @Json(name = "id")
    val id: Int,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "title")
    val title: String,
    @Json(name = "overview")
    val overview: String?,
    @Json(name = "poster_path")
    val posterUrl: String?,
)