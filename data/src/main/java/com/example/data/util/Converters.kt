package com.example.data.util

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Room TypeConverters for non-primitive types.
 *
 * We serialize [Genre] lists to JSON strings for storage.
 * Architectural Decision: Keeping this simple with Gson matches our existing
 * dependency. For production apps with large datasets, consider a separate
 * Genre table with a many-to-many relationship for better query performance.
 */
/*
class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromGenreList(genres: List<Genre>): String {
        return gson.toJson(genres)
    }

    @TypeConverter
    fun toGenreList(json: String): List<Genre> {
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}*/
