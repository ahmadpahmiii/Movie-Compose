package com.example.data.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Room TypeConverters for non-primitive types.
 *
 * We serialize lists to JSON strings for storage.
 * Architectural Decision: Keeping this simple with Gson matches our existing
 * dependency. For production apps with large datasets, consider a separate
 * table for better query performance.
 */
class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromIntList(value: List<Int>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}
