package com.example.newspaper.database.local

import androidx.room.TypeConverter
import com.example.newspaper.data.Source
import com.google.gson.Gson

class Converters {

//    data class Source(
//    val id: String,
//    val name: String
//    )

    @TypeConverter
    fun fromSource(source: Source): String =
        Gson().toJson(source)      // set in the database as a string

    @TypeConverter
    fun toSource(stringSource: String) : Source =
        Gson().fromJson(stringSource, Source::class.java)      // get it from the database and compose it into its original datatype
}