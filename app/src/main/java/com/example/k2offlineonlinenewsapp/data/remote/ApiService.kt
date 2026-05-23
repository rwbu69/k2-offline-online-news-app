package com.example.k2offlineonlinenewsapp.data.remote

import retrofit2.http.*

interface ApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String = "technology",
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "133f97b6717f41239ba7bd8960bbf2d3"
    ): NewsResponse
}