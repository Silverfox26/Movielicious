/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.surface4pro.movielicious.model.Movie;

import java.util.List;

public class MovieRepository {

    // Member variable declarations
    private final MovieDao mMovieDao;
    private final LiveData<List<Movie>> mMoviesMostPopular;
    private final LiveData<List<Movie>> mMoviesTopRated;
    private final LiveData<List<Movie>> mMovieFavorites;

    public MovieRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mMoviesMostPopular = mMovieDao.getMostPopularMovies();
        mMoviesTopRated = mMovieDao.getTopRatedMovies();
        mMovieFavorites = mMovieDao.getFavoriteMovies();
    }

    // Wrapper to get the list of most popular movies.
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Movie>> getMostPopularMovies() {
        return mMoviesMostPopular;
    }

    // Wrapper to get the list of top rated movies.
    public LiveData<List<Movie>> getTopRatedMovies() {
        return mMoviesTopRated;
    }

    // Wrapper to get the list of favorite movies.
    public LiveData<List<Movie>> getFavoriteMovies() {
        return mMovieFavorites;
    }

    // Wrapper to get a movie based on its ID.
    public Movie getMovieById(int id) {
        return mMovieDao.getMovieById(id);
    }

    // Wrapper to delete a movie based on its ID.
    public void deleteWithOrigin(int origin) {
        mMovieDao.deleteWithOrigin(origin);
    }

    // Wrapper to bulk insert movies
    public void insertMovies(List<Movie> movies) {
        mMovieDao.bulkInsertMovies(movies);
    }

    // Wrapper to insert a favorite movie into the db
    public void insertFavoriteMovie(Movie movie) {
        mMovieDao.insertFavoriteMovie(movie);
    }

    // Wrapper to delete a favorite movie from the db
    public void deleteFavorite(int movieId) {
        mMovieDao.deleteFavorite(movieId);
    }

    // Wrapper to check if the movie is already marked as favorite
    public boolean isMovieFavorite(int movieId) {
        return mMovieDao.isMovieFavorite(movieId);
    }

    // Wrapper to check if the movies of a certain origin already exist in the database
    public boolean doesOriginExist(int origin) {
        return mMovieDao.doesOriginExist(origin);
    }
}