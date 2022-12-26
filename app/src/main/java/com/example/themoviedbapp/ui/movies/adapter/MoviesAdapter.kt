package com.example.themoviedbapp.ui.movies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbapp.databinding.ItemMovieBinding
import com.example.themoviedbapp.models.entity.Movie
import com.example.themoviedbapp.ui.movies.MovieItemViewModel

class MoviesAdapter(
    private val listener: OnItemClickListener,
    private val basePosterPath: String
) :
    PagingDataAdapter<Movie, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    private var inflater: LayoutInflater? = null

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id // todo add valid compare
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? MovieViewHolder)?.bind(
            listener = listener,
            movie = getItem(position),
            basePosterPath = basePosterPath
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMovieBinding.inflate(getInflater(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    private fun getInflater(context: Context): LayoutInflater {
        return inflater ?: LayoutInflater.from(context).also {
            inflater = it
        }
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: OnItemClickListener, movie: Movie?, basePosterPath: String) {
            binding.apply {
                viewModel = MovieItemViewModel().apply { start(movie, basePosterPath) }
                root.setOnClickListener { movie?.id?.let { listener.onClick(it) } }
                executePendingBindings()
            }
        }

    }

    interface OnItemClickListener {
        fun onClick(movieId: Int)
    }
}