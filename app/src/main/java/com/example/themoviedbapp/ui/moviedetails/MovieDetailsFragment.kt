package com.example.themoviedbapp.ui.moviedetails

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.themoviedbapp.R
import com.example.themoviedbapp.databinding.FragmentMovieDetailsBinding
import com.example.themoviedbapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment<FragmentMovieDetailsBinding>() {

    private val viewModel by activityViewModels<MovieDetailsViewModel>()
    private val args: MovieDetailsFragmentArgs by navArgs()

    override val contentViewId: Int
        get() = R.layout.fragment_movie_details

    override fun setupBinding(binding: FragmentMovieDetailsBinding) {
        super.setupBinding(binding)
        binding.apply {
            viewModel = this@MovieDetailsFragment.viewModel.apply { getMovie(args.movieId) }
        }
    }
}