package com.example.themoviedbapp.data.repository

import androidx.paging.*
import com.example.themoviedbapp.data.local.db.MovieDao
import com.example.themoviedbapp.data.repository.paged.MoviesMediator
import com.example.themoviedbapp.di.IoDispatcher
import com.example.themoviedbapp.models.entity.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class MoviesRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val moviesMediator: MoviesMediator,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    private val pagingConfig = PagingConfig(pageSize = 30)

    fun getMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory: () -> PagingSource<Int, Movie> = { movieDao.getMoviesPaged() }
        val pager = Pager(
            config = pagingConfig,
            remoteMediator = moviesMediator,
            pagingSourceFactory = pagingSourceFactory,
        )
        return pager.flow.flowOn(defaultDispatcher)
    }

    suspend fun getMovieById(id: Int): Movie? {
        return withContext(defaultDispatcher) {
            movieDao.getMovieById(id)
        }
    }
}