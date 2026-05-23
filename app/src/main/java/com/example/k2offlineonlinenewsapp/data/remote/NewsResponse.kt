package com.example.k2offlineonlinenewsapp.data.remote

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)