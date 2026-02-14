package com.example.omdbtv.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.omdbtv.R
import com.example.omdbtv.api.RetrofitClient
import com.example.omdbtv.api.OmdbApiService
import com.example.omdbtv.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import com.example.omdbtv.ui.search.SearchActivity
import com.example.omdbtv.ui.genres.GenresActivity
import com.example.omdbtv.ui.detail.MovieDetailActivity
import com.example.omdbtv.ui.settings.SettingsActivity
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieCardAdapter
    private lateinit var menuAdapter: MenuAdapter

    private val menuItems = listOf(
        MenuItem.SEARCH,
        MenuItem.GENRES,
        MenuItem.POPULAR,
        MenuItem.SETTINGS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        com.example.omdbtv.api.RetrofitClient.init(this)

        setupMenu()
        setupMoviesGrid()
        loadPopularMovies()
    }

    private fun setupMenu() {
        menuAdapter = MenuAdapter(menuItems) { menuItem ->
            when (menuItem) {
                MenuItem.SEARCH -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                }
                MenuItem.GENRES -> {
                    startActivity(Intent(this, GenresActivity::class.java))
                }
                MenuItem.POPULAR -> {
                    loadPopularMovies()
                }
                MenuItem.SETTINGS -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
        }

        binding.mainMenu.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = menuAdapter
        }
    }

    private fun setupMoviesGrid() {
        movieAdapter = MovieCardAdapter { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra("imdb_id", movie.imdbID)
            }
            startActivity(intent)
        }

        binding.moviesGrid.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 5)
            adapter = movieAdapter
        }
    }

    private fun loadPopularMovies() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.searchMovies(
                    apiKey = RetrofitClient.API_KEY,
                    query = "Marvel"
                )
                if (response.isSuccessful) {
                    val movies = response.body()?.Search ?: emptyList()
                    movieAdapter.submitList(movies)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    enum class MenuItem(val titleRes: Int) {
        SEARCH(R.string.search),
        GENRES(R.string.genres_title),
        POPULAR(R.string.popular_title),
        SETTINGS(R.string.settings_title)
    }

    class MenuAdapter(
        private val items: List<MenuItem>,
        private val onItemClick: (MenuItem) -> Unit
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

        private var selectedPosition = 0

        inner class MenuViewHolder(view: View) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.menu_text)
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): MenuViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu, parent, false)
            return MenuViewHolder(view)
        }

        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            val item = items[position]
            holder.textView.text = holder.itemView.context.getString(item.titleRes)
            holder.textView.isSelected = position == selectedPosition

            holder.itemView.setOnClickListener {
                onItemClick(item)
            }

            holder.itemView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    selectedPosition = position
                    notifyDataSetChanged()
                }
            }
        }

        override fun getItemCount() = items.size
    }
}
