package com.prabhakaran.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class NewsResponse(
    val category: String,
    val `data`: List<News>,
    val success: Boolean
)

@Entity
data class News(
    val author: String,
    val content: String,
    val date: String,
    @PrimaryKey
    val id: String,
    val imageUrl: String,
    val readMoreUrl: String?,
    val time: String,
    val title: String,
    val url: String
)