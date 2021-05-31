package com.example.fr.news_app

import android.app.Application
import com.example.fr.news_app.di.*

class AppController : Application() {

    lateinit var mApiComponent: ApiComponent

    override fun onCreate() {
        super.onCreate()
        app = this

        mApiComponent = DaggerApiComponent.builder()
            .appModule(AppModule(this))
            .apiHelper(ApiHelper())
            .dBModule(DBModule(this))
            .build()
    }

    companion object {
        lateinit var app: AppController
            private set
    }

}