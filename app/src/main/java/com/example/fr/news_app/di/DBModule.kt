package com.example.fr.news_app.di

import android.app.Application
import androidx.room.Room
import com.example.fr.news_app.room.AppDB
import com.example.fr.news_app.room.NewsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Suppress("SpellCheckingInspection")
@Module(includes = [AppModule::class])
class DBModule(mApplication: Application) {
    private val newsDB: AppDB =
        Room.databaseBuilder(mApplication, AppDB::class.java, "GoogleNews.sqlite").build()

    @Singleton
    @Provides
    internal fun providesRoomDatabase(): AppDB {
        return newsDB
    }

    @Singleton
    @Provides
    internal fun providesProductDao(newsDB: AppDB): NewsDao {
        return newsDB.newsDao()
    }
}