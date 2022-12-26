package com.example.themoviedbapp.ui.movies

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.themoviedbapp.models.entity.Movie

class MovieItemViewModel : ViewModel() {

    var title = ObservableField<String>()
    var releaseDate = ObservableField<String>()
    var posterUrl = ObservableField<String>()

    fun start(movie: Movie?, basePosterPath: String) {
        title.set(movie?.title)
        releaseDate.set(movie?.releaseDate)
        posterUrl.set(basePosterPath + movie?.posterUrl)
    }
}