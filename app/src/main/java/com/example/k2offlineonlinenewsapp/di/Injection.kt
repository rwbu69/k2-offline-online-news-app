package com.example.k2offlineonlinenewsapp.di

import android.content.Context
import com.example.k2offlineonlinenewsapp.data.NewsRepository
import com.example.k2offlineonlinenewsapp.data.local.NewsDatabase
import com.example.k2offlineonlinenewsapp.data.remote.ApiConfig

object Injection {

    fun provideRepository(context: Context): NewsRepository{
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getDatabase(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}