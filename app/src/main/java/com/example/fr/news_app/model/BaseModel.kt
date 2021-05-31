package com.example.fr.news_app.model

data class BaseModel<T>(
    val status: String?,
    val message: String?,
    val articles: T
)
