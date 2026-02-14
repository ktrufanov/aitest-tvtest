package com.example.omdbtv.ui.detail

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.omdbtv.R
import com.example.omdbtv.api.RetrofitClient
import com.example.omdbtv.databinding.ActivityMovieDetailBinding
import kotlinx.coroutines.launch

class MovieDetailActivity : FragmentActivity() {

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        loadMovieDetails()
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadMovieDetails() {
        val imdbId = intent.getStringExtra("imdb_id") ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getMovieDetails(
                    apiKey = RetrofitClient.API_KEY,
                    imdbId = imdbId
                )

                if (response.isSuccessful) {
                    val movie = response.body()
                    movie?.let { displayMovieDetails(it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayMovieDetails(movie: com.example.omdbtv.api.MovieDetails) {
        binding.apply {
            titleText.text = movie.Title
            yearText.text = movie.Year
            ratingText.text = "IMDB: ${movie.imdbRating}"
            runtimeText.text = movie.Runtime
            genreText.text = movie.Genre
            plotText.text = movie.Plot
            directorText.text = movie.Director
            actorsText.text = movie.Actors

            Glide.with(this@MovieDetailActivity)
                .load(movie.Poster)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(posterImage)

            Glide.with(this@MovieDetailActivity)
                .load(movie.Poster)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(backdropImage)
        }
    }
}
