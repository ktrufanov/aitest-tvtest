package com.example.omdbtv.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.omdbtv.R
import com.example.omdbtv.api.MovieSearchResult

class MovieCardAdapter(
    private val onMovieClick: (MovieSearchResult) -> Unit
) : ListAdapter<MovieSearchResult, MovieCardAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_card, parent, false)
        return MovieViewHolder(view, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(
        view: View,
        private val onMovieClick: (MovieSearchResult) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val posterImage: ImageView = view.findViewById(R.id.movie_poster)
        private val titleText: TextView = view.findViewById(R.id.movie_title)

        fun bind(movie: MovieSearchResult) {
            titleText.text = "${movie.Title} (${movie.Year})"

            Glide.with(itemView.context)
                .load(movie.Poster)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(posterImage)

            itemView.setOnClickListener {
                onMovieClick(movie)
            }

            itemView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
                } else {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                }
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieSearchResult>() {
        override fun areItemsTheSame(oldItem: MovieSearchResult, newItem: MovieSearchResult): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: MovieSearchResult, newItem: MovieSearchResult): Boolean {
            return oldItem == newItem
        }
    }
}
