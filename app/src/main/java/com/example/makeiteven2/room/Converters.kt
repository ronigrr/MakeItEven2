package com.example.makeiteven2.room

import androidx.room.TypeConverter
import com.example.makeiteven2.data_models.StageInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.collections.ArrayList

class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<StageInfo>? {
        val listType: Type = object : TypeToken<ArrayList<StageInfo?>?>() {}.type
        return value?.let { Gson().fromJson(it, listType) }
    }

    @TypeConverter
    fun listToString(value: ArrayList<StageInfo>?): String? {
        return Gson().toJson(value)
    }

}