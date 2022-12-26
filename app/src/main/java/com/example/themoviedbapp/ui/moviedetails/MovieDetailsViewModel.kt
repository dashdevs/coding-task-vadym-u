package com.example.themoviedbapp.ui.moviedetails

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedbapp.data.repository.MoviesRepository
import com.example.themoviedbapp.di.NetworkModule.BasePosterPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @BasePosterPath private val basePosterPath: String
) : ViewModel() {

    var title = ObservableField<String>()
    var releaseDate = ObservableField<String>()
    var overview = ObservableField<String>()
    var posterUrl = ObservableField<String>()

    fun getMovie(movieId: Int) {
        if (movieId > 0) {
            viewModelScope.launch {
                moviesRepository.getMovieById(movieId)?.let { movie ->
                    title.set(movie.title)
                    releaseDate.set(movie.releaseDate)
                    overview.set(movie.overview)
                    posterUrl.set(basePosterPath + movie.posterUrl)
                } ?: run {
                    // todo add empty state
                }
            }
        } else {
            // todo add empty state
        }
    }
}