package com.example.newspaper.database.api

import com.example.newspaper.data.NewsResponse
import com.example.newspaper.utils.Constraints.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
/** Using the {Request parameters} **/

    @GET("top-headlines")
    suspend fun getNewsHeadlines(
        @Query("country") countryCode: String = "eg",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY,
    )
    : Response<NewsResponse>

    @GET("everything")
    suspend fun searchFor(
        @Query("q") searchKey: String,
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY,
    )
    : Response<NewsResponse>

}

