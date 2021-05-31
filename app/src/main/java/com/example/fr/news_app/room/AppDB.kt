package com.example.fr.news_app.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fr.news_app.model.DataConverter
import com.example.fr.news_app.model.NewsData

@TypeConverters(DataConverter::class)
@Database(entities = [NewsData::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}