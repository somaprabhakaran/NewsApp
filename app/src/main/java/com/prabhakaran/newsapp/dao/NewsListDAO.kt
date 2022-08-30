package com.prabhakaran.newsapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.prabhakaran.newsapp.model.News

@Dao
interface NewsListDAO {

    @Insert(onConflict = REPLACE)
    fun insertNewsList(news: List<News>)

    @Query("SELECT * FROM News")
    fun getNews() : List<News>

    @Update
    fun getUpdate(news: News)
}