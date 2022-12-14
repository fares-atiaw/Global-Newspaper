package com.example.newspaper.database.api

import com.example.newspaper.utils.Constraints.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        val retrofit by lazy {
            val logging = HttpLoggingInterceptor().run {        // to log responses of retrofit (for debugging cases)
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val newsAPI : EndPoints by lazy {
            retrofit.create(EndPoints::class.java)
        }
    }
}