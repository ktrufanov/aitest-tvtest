package com.example.omdbtv.ui.genres

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.omdbtv.api.RetrofitClient
import com.example.omdbtv.databinding.ActivitySearchBinding
import com.example.omdbtv.ui.MovieCardAdapter
import com.example.omdbtv.ui.detail.MovieDetailActivity
import kotlinx.coroutines.launch

class SearchResultsActivity : FragmentActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var movieAdapter: MovieCardAdapter
    private var currentPage = 1
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val genre = intent.getStringExtra("genre") ?: ""
        val title = intent.getStringExtra("title") ?: ""

        binding.searchInput.setText(title)
        binding.searchInput.isEnabled = false

        setupRecyclerView()
        setupBackButton()

        // Загружаем фильмы по жанру (поиск популярных фильмов этого жанра)
        currentQuery = when (genre) {
            "Action" -> "Marvel"
            "Adventure" -> "Indiana Jones"
            "Animation" -> "Pixar"
            "Comedy" -> "Comedy"
            "Crime" -> "Godfather"
            "Documentary" -> "Documentary"
            "Drama" -> "Drama"
            "Family" -> "Disney"
            "Fantasy" -> "Harry Potter"
            "Horror" -> "Horror"
            "Mystery" -> "Sherlock"
            "Romance" -> "Romance"
            "Sci-Fi" -> "Star Wars"
            "Thriller" -> "Thriller"
            "War" -> "War"
            "Western" -> "Western"
            else -> "Movie"
        }

        loadMovies()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieCardAdapter { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra("imdb_id", movie.imdbID)
            }
            startActivity(intent)
        }

        binding.searchResults.apply {
            layoutManager = GridLayoutManager(this@SearchResultsActivity, 5)
            adapter = movieAdapter
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadMovies() {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.searchMovies(
                    apiKey = RetrofitClient.API_KEY,
                    query = currentQuery,
                    page = currentPage
                )

                if (response.isSuccessful) {
                    val movies = response.body()?.Search ?: emptyList()
                    movieAdapter.submitList(movies)
                    binding.emptyText.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
                } else {
                    binding.emptyText.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.emptyText.visibility = View.VISIBLE
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
