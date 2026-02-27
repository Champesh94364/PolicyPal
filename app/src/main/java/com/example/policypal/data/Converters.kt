package com.example.policypal.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun customFieldsToString(value: List<CustomField>?): String {
        return gson.toJson(value ?: emptyList<CustomField>())
    }

    @TypeConverter
    fun stringToCustomFields(value: String?): List<CustomField> {
        if (value.isNullOrBlank()) return emptyList()
        val type = object : TypeToken<List<CustomField>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun checklistToString(value: List<Boolean>?): String {
        return gson.toJson(value ?: emptyList<Boolean>())
    }

    @TypeConverter
    fun stringToChecklist(value: String?): List<Boolean> {
        if (value.isNullOrBlank()) return emptyList()
        val type = object : TypeToken<List<Boolean>>() {}.type
        return gson.fromJson(value, type)
    }
}