package com.example.newspaper.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String, // ok
    val totalResults: Int // 383
)

@Entity(tableName = "articles")
data class Article(
//    @PrimaryKey(autoGenerate = true)
//    val id : Int? = null,      // tried: var, Int, Int = 0, Int?
    val author: String?,
    val content: String?,
    val description: String?,
    @ColumnInfo(name = "published_at")
    val publishedAt: String?,
    val source: Source,
    val title: String,
    @PrimaryKey
    val url: String,
    @ColumnInfo(name = "url_to_image")
    val urlToImage: String?
) : Serializable
{
    /*constructor(description: String, publishedAt: String, source: Source, title: String, url: String, urlToImage: String )
            : this(
        description = description,
        publishedAt = publishedAt,
        source = source,
        title = title,
        url = url,
        urlToImage = urlToImage
    )*/

    override fun hashCode(): Int {
        var result = url.hashCode()
        if(url.isNullOrEmpty()){
            result = 31 * result + url.hashCode()
        }
        return result
    }

}

data class Source(
    val id: String?, // business-insider
    val name: String // The Guardian
)

