package com.example.surface4pro.movielicious;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Converters {

    @TypeConverter
    public static int[] fromString(String value) {
        Type arrayType = new TypeToken<int[]>() {
        }.getType();
        return new Gson().fromJson(value, arrayType);
    }

    @TypeConverter
    public static String fromIntArray(int[] array) {
        Gson gson = new Gson();
        return gson.toJson(array);
    }

}
