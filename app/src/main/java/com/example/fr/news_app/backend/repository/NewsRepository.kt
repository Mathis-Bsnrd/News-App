package com.example.fr.news_app.backend.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.fr.news_app.AppController
import com.example.fr.news_app.backend.api.NewsApi
import com.example.fr.news_app.di.ApiComponent
import com.example.fr.news_app.model.BaseModel
import com.example.fr.news_app.model.NewsData
import com.example.fr.news_app.room.NewsDao
import com.example.fr.news_app.util.Constants
import com.example.fr.news_app.util.Utils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class NewsRepository {

    private val newsListLiveData: MutableLiveData<BaseModel<ArrayList<NewsData>>> = MutableLiveData()

    init {
        val apiComponent: ApiComponent = AppController.app.mApiComponent
        apiComponent.inject(this)
    }

    @Inject
    lateinit var newsApi: NewsApi

    @Inject
    lateinit var newsDao: NewsDao

    fun fetchNewsList(): MutableLiveData<BaseModel<ArrayList<NewsData>>> {

        fetchNewsOffline()

        if (!Utils.isNetworkAvailable()) {
            Toast.makeText(
                AppController.app.applicationContext,
                "Internet non-connecté",
                Toast.LENGTH_LONG
            ).show()
        }

        val postListInfo: Call<BaseModel<ArrayList<NewsData>>> =
            newsApi.getPaymentTypes("fr", Constants.NEWS_API_KEY, 50)
        postListInfo.enqueue(object : Callback<BaseModel<ArrayList<NewsData>>> {
            override fun onFailure(call: Call<BaseModel<ArrayList<NewsData>>>, t: Throwable) {
                Toast.makeText(
                    AppController.app.applicationContext,
                    "Erreur de chargement",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<BaseModel<ArrayList<NewsData>>>,
                response: Response<BaseModel<ArrayList<NewsData>>>
            ) {
                if (response.code() == 200) {
                    insertNews(response.body()?.articles)
                } else {
                    Toast.makeText(
                        AppController.app.applicationContext,
                        "Erreur de chargement",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
        return newsListLiveData
    }

    private fun fetchNewsOffline() {
        doAsync {
            val result = newsDao.getNews()
            uiThread {
                val newsArrayList = ArrayList<NewsData>()
                newsArrayList.addAll(result)
                val dbData = BaseModel("ok", "", newsArrayList)
                newsListLiveData.postValue(dbData)
            }
        }
    }

    fun insertNews(newsList: ArrayList<NewsData>?) {
        doAsync {

            var needsUpdate = false
            if (newsList != null) {
                for (item in newsList) {
                    val inserted = newsDao.insertNews(item)
                    if (inserted == -1L) {
                        val updated = newsDao.insertNews(item)
                        if (updated > 0) {
                            needsUpdate = true
                        }
                    } else {
                        needsUpdate = true
                    }
                }
            }

            uiThread {
                if (needsUpdate)
                    fetchNewsOffline()
            }
        }
    }


}