package com.example.newspaper.database.local

import androidx.room.TypeConverter
import com.example.newspaper.data.Source

class Converters {

//    data class Source(
//    val id: String,
//    val name: String
//    )

    @TypeConverter
    fun fromSource(source: Source) = source.name      // set in the database as a string

    @TypeConverter
    fun toSource(name: String) = Source("-", name)      // get it from the database and compose it into its original datatype
}