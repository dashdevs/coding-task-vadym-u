package com.example.themoviedbapp.data.repository.paged

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.themoviedbapp.data.local.db.AppDatabase
import com.example.themoviedbapp.data.remote.Api
import com.example.themoviedbapp.data.remote.ApiResponse
import com.example.themoviedbapp.models.entity.Movie
import com.example.themoviedbapp.models.entity.MovieRemoteKeys
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MoviesMediator @Inject constructor(
    private val api: Api,
    private val db: AppDatabase
) : RemoteMediator<Int, Movie>() {

    private val movieDao = db.movieDao()
    private val movieRemoteKeysDao = db.movieRemoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        return if (movieDao.getMoviesCount() > 0) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }
            val response = api.getMovies(page = page)

            var endOfPaginationReached = false

            when (response) {
                is ApiResponse.Error -> MediatorResult.Error(Throwable("Api Error"))
                is ApiResponse.Success -> {
                    val body = response.body
                    val list = body.movieList

                    endOfPaginationReached = body.page == body.totalPages
                    db.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            movieDao.deleteAllMovies()
                            movieRemoteKeysDao.deleteAllMovieRemoteKeys()
                        }

                        val pageNumber = body.page
                        val nextPage = pageNumber + 1
                        val prevPage = if (pageNumber <= 1) null else pageNumber - 1

                        val keys = list.map { movie ->
                            MovieRemoteKeys(
                                id = movie.id,
                                prevPage = prevPage,
                                nextPage = nextPage
                            )
                        }
                        movieRemoteKeysDao.addAllMovieRemoteKeys(movieRemoteKeys = keys)
                        movieDao.addMovies(movies = list)
                    }
                }
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>,
    ): MovieRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                movieRemoteKeysDao.getMovieRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Movie>,
    ): MovieRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                movieRemoteKeysDao.getMovieRemoteKeys(id = movie.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Movie>,
    ): MovieRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                movieRemoteKeysDao.getMovieRemoteKeys(id = movie.id)
            }
    }
}