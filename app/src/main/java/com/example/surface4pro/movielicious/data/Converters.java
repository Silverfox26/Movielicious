/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * This class contains converters for the Room database
 */
public class Converters {

    /**
     * Converts a JSON int array string to an int array.
     *
     * @param value The encoded int array JSON string.
     * @return The converted int array.
     */
    @TypeConverter
    public static int[] fromString(String value) {
        Type arrayType = new TypeToken<int[]>() {
        }.getType();
        return new Gson().fromJson(value, arrayType);
    }

    /**
     * Converts an int array to a JSON string representation.
     *
     * @param array The int array to be converted.
     * @return The converted JSON String.
     */
    @TypeConverter
    public static String fromIntArray(int[] array) {
        Gson gson = new Gson();
        return gson.toJson(array);
    }
}