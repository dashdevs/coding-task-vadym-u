package com.example.themoviedbapp.di

import com.example.themoviedbapp.data.local.db.MovieDao
import com.example.themoviedbapp.data.remote.Api
import com.example.themoviedbapp.data.repository.MoviesRepository
import com.example.themoviedbapp.data.repository.paged.MoviesMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideBreedsRepository(
        movieDao: MovieDao,
        moviesMediator: MoviesMediator,
        @IoDispatcher defaultDispatcher: CoroutineDispatcher
    ): MoviesRepository {
        return MoviesRepository(movieDao, moviesMediator, defaultDispatcher)
    }
}
