package com.example.newspaper.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class NewsResponse(
    val articles: List<Article>,
    val status: String, // ok
    val totalResults: Int // 383
)

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val author: String, // Nigel Slater
    val content: String, // The oven has always been at the heart of this kitchen. The glow from its glass door fills the space with a sense of warmth and hospitality. There are opportunities an oven can provide that are differ… [+11926 chars]
    val description: String, // Energy consumption will be more important than ever this winter. These dishes are heat-efficient – and deliciousThe oven has always been at the heart of this kitchen. The glow from its glass door fills the space with a sense of warmth and hospitality. There a…
    val publishedAt: String, // 2022-10-17T07:00:58Z
    val source: Source,
    val title: String, // Smoked haddock, baked gammon: Nigel Slater’s winter recipes that make the most of your oven
    val url: String, // https://www.theguardian.com/food/2022/oct/17/smoked-haddock-baked-gammon-nigel-slater-winter-recipes-that-make-the-most-of-your-oven
    val urlToImage: String // https://i.guim.co.uk/img/media/5686d5d8e09eb971748443bc3851de1bff573f8d/0_385_5042_3024/master/5042.jpg?width=1200&height=630&quality=85&auto=format&fit=crop&overlay-align=bottom%2Cleft&overlay-width=100p&overlay-base64=L2ltZy9zdGF0aWMvb3ZlcmxheXMvdG8tZGVmYXVsdC5wbmc&enable=upscale&s=d8cb7327423b362d39b5d2799e39a2b4
)

data class Source(
    val id: String, // business-insider
    val name: String // The Guardian
)

