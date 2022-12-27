package com.example.themoviedbapp.models.network

import com.example.themoviedbapp.models.entity.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDto(
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
) {
    fun toMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            releaseDate = releaseDate,
            overview = overview,
            posterUrl = posterUrl
        )
    }
}