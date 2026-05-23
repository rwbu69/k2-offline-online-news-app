package com.example.k2offlineonlinenewsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val url: String?,
    val sourceName: String?
)

