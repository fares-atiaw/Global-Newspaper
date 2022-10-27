package com.example.newspaper.database

import com.example.newspaper.data.Article
import com.example.newspaper.database.api.RetrofitInstance
import com.example.newspaper.database.local.ArticleDatabase

class NewsRepository(val db: ArticleDatabase)
{
    /**Remote**/
    suspend fun getBreakingNews(country_of_2Letters : String, pageNum : Int) =
        RetrofitInstance.newsAPI.getNewsHeadlines()

    suspend fun getSearchedNews(text : String, pageNum : Int) =
        RetrofitInstance.newsAPI.searchFor(text, pageNum)

    /**Local**/
    suspend fun addIfNotExist(article : Article) = db.newsDatabaseDao.upsert(article)

    fun getSavedNews() = db.newsDatabaseDao.getAllArticles()

    suspend fun deleteArticle(article : Article) = db.newsDatabaseDao.deleteArticle(article)

}