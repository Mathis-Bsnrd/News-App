package com.example.fr.news_app.di

import com.example.fr.news_app.backend.api.NewsApi
import com.example.fr.news_app.backend.repository.NewsRepository
import com.example.fr.news_app.viewmodel.NewsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiHelper::class, AppModule::class, DBModule::class])
interface ApiComponent {

    val newsApi: NewsApi

    fun inject(repo: NewsRepository)
    fun inject(newsVM: NewsViewModel)
}