package com.example.omdbtv.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {

    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int = 1
    ): Response<SearchResponse>

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full"
    ): Response<MovieDetails>

    @GET("/")
    suspend fun searchByGenre(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int = 1
    ): Response<SearchResponse>
}
