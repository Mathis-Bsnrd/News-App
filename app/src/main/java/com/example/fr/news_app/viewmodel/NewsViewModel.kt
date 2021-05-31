package com.example.fr.news_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fr.news_app.AppController
import com.example.fr.news_app.backend.repository.NewsRepository
import com.example.fr.news_app.model.BaseModel
import com.example.fr.news_app.model.NewsData
import java.util.*
import javax.inject.Inject


class NewsViewModel : ViewModel() {

    private var newsLiveData: MutableLiveData<BaseModel<ArrayList<NewsData>>>

    init {
        this.newsLiveData = MutableLiveData()
        AppController.app.mApiComponent.inject(this)
    }

    @Inject
    lateinit var mRepository: NewsRepository

    fun getNewsList(): MutableLiveData<BaseModel<ArrayList<NewsData>>> {
        newsLiveData = mRepository.fetchNewsList()
        return newsLiveData
    }

}