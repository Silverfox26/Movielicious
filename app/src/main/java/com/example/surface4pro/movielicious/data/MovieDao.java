package com.example.surface4pro.movielicious.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.surface4pro.movielicious.model.Movie;

import java.util.ArrayList;

@Dao
public interface MovieDao {
    @Query("SELECT * from movie_table ORDER BY id ASC")
    ArrayList<Movie> getAllMovies();

    @Query("SELECT * from movie_table WHERE origin = :origin ORDER BY id ASC")
    LiveData<ArrayList<Movie>> getMoviesByOrigin(int origin);

    @Query("DELETE from movie_table WHERE origin = :origin")
    void deleteWithOrigin(int origin);

    @Query("DELETE from movie_table WHERE origin = :origin AND movie_id = :movieId")
    void deleteFavorite(int movieId, int origin);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertMovies(Movie... movie);
}