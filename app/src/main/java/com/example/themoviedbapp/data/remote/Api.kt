package com.example.themoviedbapp.data.remote

import com.example.themoviedbapp.models.network.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

typealias Response<T> = ApiResponse<T, Unit>

interface Api {

    @GET(GET_MOVIES_PATH)
    suspend fun getMovies(@Query(PAGE_QUERY) page: Int): Response<MovieResponse>
}