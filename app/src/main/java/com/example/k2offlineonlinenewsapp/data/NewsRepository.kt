package com.example.k2offlineonlinenewsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.k2offlineonlinenewsapp.data.local.NewsDao
import com.example.k2offlineonlinenewsapp.data.local.NewsEntity
import com.example.k2offlineonlinenewsapp.data.remote.ApiService

class NewsRepository(
    private val apiService: ApiService, private val newsDao: NewsDao

) {
    fun getNews(): LiveData<List<NewsEntity>> {
        return newsDao.getAllNews()
    }

    suspend fun refreshNews() {
        try {
            val response = apiService.getTopHeadlines()
            val articles = response.articles

            // Filter articles with missing essential info if necessary, or map all
            val newsList = articles.map { article ->
                NewsEntity(
                    title = article.title,
                    description = article.description,
                    urlToImage = article.urlToImage,
                    publishedAt = article.publishedAt,
                    url = article.url,
                    sourceName = article.source?.name
                )
            }

            newsDao.deleteAllNews()
            newsDao.insertNews(newsList)

        } catch (e: Exception) {
            Log.e("NewsRepository", "refreshNews: ${e.message.toString()}", e)
            throw e
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(apiService: ApiService, newsDao: NewsDao): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao)
            }.also { instance = it }
    }
}


