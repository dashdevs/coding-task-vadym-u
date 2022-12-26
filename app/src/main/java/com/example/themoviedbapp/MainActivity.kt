package com.example.themoviedbapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.themoviedbapp.tools.extensions.observeCommandSafety
import com.example.themoviedbapp.ui.movies.MoviesFragment
import com.example.themoviedbapp.ui.movies.MoviesFragmentDirections
import com.example.themoviedbapp.ui.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MoviesViewModel>()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        subscribeToActionsMoviesViewModel(viewModel)
    }

    private fun subscribeToActionsMoviesViewModel(viewModel: MoviesViewModel) {
        viewModel.apply {
            observeCommandSafety(startMovieDetailsScreenCommand) {
                navController.navigate(
                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(
                        it
                    )
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}