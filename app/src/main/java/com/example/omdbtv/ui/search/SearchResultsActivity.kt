package com.example.omdbtv.ui.search

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title") ?: getString(com.example.omdbtv.R.string.search_title)

        setupRecyclerView()
        setupBackButton()

        // Скрываем поле поиска, показываем только результаты
        binding.searchInput.visibility = android.view.View.GONE

        val query = intent.getStringExtra("query") ?: ""
        performSearch(query)
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

    private fun performSearch(query: String) {
        if (query.isEmpty()) return

        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.emptyText.visibility = android.view.View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.searchMovies(
                    apiKey = RetrofitClient.API_KEY,
                    query = query
                )

                if (response.isSuccessful) {
                    val movies = response.body()?.Search ?: emptyList()
                    movieAdapter.submitList(movies)
                    binding.emptyText.visibility = if (movies.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
                } else {
                    binding.emptyText.visibility = android.view.View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.emptyText.visibility = android.view.View.VISIBLE
            } finally {
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
}
