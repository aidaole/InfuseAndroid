package com.aidaole.infuseandroid.data.model

data class Movie(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val runtime: Int? = null,
    val genres: List<Genre> = emptyList()
)

data class Genre(
    val id: Int,
    val name: String
)

data class MovieSearchResult(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
) 