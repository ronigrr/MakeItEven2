package com.example.makeiteven2.room

import androidx.room.TypeConverter
import com.example.makeiteven2.data_models.NameAndScoreInfo
import com.example.makeiteven2.data_models.StageInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

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

    @TypeConverter
    fun fromStringToNameAndScoreInfo(value: String?): ArrayList<NameAndScoreInfo>? {
        val listType: Type = object : TypeToken<ArrayList<NameAndScoreInfo?>?>() {}.type
        return value?.let { Gson().fromJson(it, listType) }
    }

    @TypeConverter
    fun nameAndScoreInfoListToString(value: ArrayList<NameAndScoreInfo>?): String? {
        return Gson().toJson(value)
    }
}