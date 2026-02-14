package com.example.omdbtv.ui.genres

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omdbtv.R
import com.example.omdbtv.databinding.ActivityGenresBinding
import com.example.omdbtv.ui.search.SearchResultsActivity

class GenresActivity : FragmentActivity() {

    private lateinit var binding: ActivityGenresBinding
    private lateinit var genreAdapter: GenreAdapter

    // OMDB API не поддерживает прямой поиск по жанру,
    // поэтому используем ключевые слова для популярных фильмов в каждом жанре
    private val genres = listOf(
        Genre("Боевик", "Action", R.color.primary),
        Genre("Приключения", "Adventure", R.color.primary_dark),
        Genre("Комедия", "Comedy", R.color.accent),
        Genre("Криминал", "Crime", R.color.surface),
        Genre("Драма", "Drama", R.color.card_background),
        Genre("Фэнтези", "Fantasy", R.color.primary),
        Genre("Ужасы", "Horror", R.color.primary_dark),
        Genre("Мистика", "Mystery", R.color.accent),
        Genre("Мелодрама", "Romance", R.color.surface),
        Genre("Фантастика", "Sci-Fi", R.color.card_background),
        Genre("Триллер", "Thriller", R.color.primary),
        Genre("Вестерн", "Western", R.color.primary_dark),
        Genre("Военный", "War", R.color.accent),
        Genre("Мультфильм", "Animation", R.color.surface),
        Genre("Документальный", "Documentary", R.color.card_background),
        Genre("Семейный", "Family", R.color.primary)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        setupGenresGrid()
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupGenresGrid() {
        genreAdapter = GenreAdapter(genres) { genre ->
            // Ищем фильмы по ключевому слову жанра
            val intent = Intent(this, SearchResultsActivity::class.java).apply {
                putExtra("query", genre.searchTerm)
                putExtra("title", genre.name)
            }
            startActivity(intent)
        }

        binding.genresGrid.apply {
            layoutManager = GridLayoutManager(this@GenresActivity, 4)
            adapter = genreAdapter
        }
    }

    data class Genre(
        val name: String,
        val searchTerm: String,
        val colorRes: Int
    )

    class GenreAdapter(
        private val genres: List<Genre>,
        private val onClick: (Genre) -> Unit
    ) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

        inner class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val genreName: TextView = view.findViewById(R.id.genre_name)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_genre, parent, false)
            return GenreViewHolder(view)
        }

        override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
            val genre = genres[position]
            holder.genreName.text = genre.name

            // Устанавливаем цвет фона
            holder.itemView.setBackgroundColor(holder.itemView.context.getColor(genre.colorRes))

            holder.itemView.setOnClickListener {
                onClick(genre)
            }

            holder.itemView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(200).start()
                } else {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                }
            }
        }

        override fun getItemCount() = genres.size
    }
}
