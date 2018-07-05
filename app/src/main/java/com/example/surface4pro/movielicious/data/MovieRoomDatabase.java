package com.example.surface4pro.movielicious.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.surface4pro.movielicious.model.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieRoomDatabase extends RoomDatabase {
    // Using the Singleton Pattern to make sure that only one instance of the database can
    // be opened at the same time.
    private static MovieRoomDatabase INSTANCE;

    public static MovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create the database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "movie_database")
                            // TODO DELETE THIS BEFORE PRODUCTION RELEASE!!!
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Abstract getter method for the MovieDao
    public abstract MovieDao movieDao();
}