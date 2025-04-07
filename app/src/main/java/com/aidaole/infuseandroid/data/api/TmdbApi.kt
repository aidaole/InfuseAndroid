package com.aidaole.infuseandroid.data.api

import com.aidaole.infuseandroid.data.model.Movie
import com.aidaole.infuseandroid.data.model.MovieSearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "zh-CN"
    ): MovieSearchResult

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "zh-CN"
    ): Movie
} 