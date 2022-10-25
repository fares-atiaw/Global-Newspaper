package com.example.newspaper.database

import com.example.newspaper.database.api.RetrofitInstance
import com.example.newspaper.database.local.ArticleDatabase

class NewsRepository(db: ArticleDatabase)
{
    suspend fun getBreakingNews(country_of_2Letters : String, pageNum : Int) =
        RetrofitInstance.newsAPI.getNewsHeadlines()
}