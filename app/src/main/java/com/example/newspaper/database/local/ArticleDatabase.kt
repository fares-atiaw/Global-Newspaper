package com.example.newspaper.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newspaper.data.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {                                   //TODO: update your documentation

    abstract val newsDatabaseDao: NewsDao       //abstract fun getArticleDao(): ArticleDao

    companion object {

        @Volatile
        private var instance: ArticleDatabase? = null

        operator fun invoke(context: Context) {
            instance ?: synchronized(this) {
                instance = createDatabase(context)
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "article_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


}
