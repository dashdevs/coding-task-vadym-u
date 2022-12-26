package com.example.themoviedbapp.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.themoviedbapp.data.repository.MoviesRepository
import com.example.themoviedbapp.di.NetworkModule.BasePosterPath
import com.example.themoviedbapp.models.entity.Movie
import com.example.themoviedbapp.tools.livedata.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @BasePosterPath private val basePosterPath: String
) : ViewModel() {

    val startMovieDetailsScreenCommand = SingleLiveEvent<Int>()

    fun getMovies(): Flow<PagingData<Movie>> {
        return moviesRepository.getMovies().cachedIn(viewModelScope)
    }

    fun getBasePosterPath() = basePosterPath

    fun onItemClick(movieId: Int) {
        startMovieDetailsScreenCommand.value = movieId
    }
}