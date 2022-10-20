package com.example.newspaper.database.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newspaper.data.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article : Article): Long      // TODO ???? Int

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>   // TODO ???? There will be two tables. one for saved articles and other for cashing.

    @Delete
    suspend fun deleteArticle(article: Article)

}