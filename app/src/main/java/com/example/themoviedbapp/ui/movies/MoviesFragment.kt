package com.example.themoviedbapp.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbapp.R
import com.example.themoviedbapp.databinding.FragmentMoviesBinding
import com.example.themoviedbapp.ui.BaseFragment
import com.example.themoviedbapp.ui.movies.adapter.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(), MoviesAdapter.OnItemClickListener {

    private val viewModel by activityViewModels<MoviesViewModel>()
    private lateinit var adapter: MoviesAdapter

    override val contentViewId: Int
        get() = R.layout.fragment_movies

    override fun setupBinding(binding: FragmentMoviesBinding) {
        super.setupBinding(binding)
        adapter = MoviesAdapter(this@MoviesFragment, viewModel.getBasePosterPath())
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = this@MoviesFragment.adapter
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchMovies()
    }

    private fun fetchMovies() {
        lifecycleScope.launch {
            viewModel.getMovies().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onClick(movieId: Int) {
        viewModel.onItemClick(movieId)
    }
}