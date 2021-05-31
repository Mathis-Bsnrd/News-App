package com.example.fr.news_app.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fr.news_app.model.NewsData

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    fun insertNews(news: NewsData?):Long

    @Query("SELECT * FROM `NewsData` order by `published_at` desc")
    fun getNews(): List<NewsData>

}