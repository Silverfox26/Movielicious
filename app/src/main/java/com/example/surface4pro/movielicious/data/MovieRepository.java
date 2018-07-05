package com.example.surface4pro.movielicious.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.surface4pro.movielicious.model.Movie;

import java.util.List;

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mMoviesMostPopular;
    private LiveData<List<Movie>> mMoviesTopRated;
    private LiveData<List<Movie>> mMovieFavorites;

    public MovieRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mMoviesMostPopular = mMovieDao.getMostPopularMovies();
        mMoviesTopRated = mMovieDao.getTopRatedMovies();
        mMovieFavorites = mMovieDao.getFavoriteMovies();
    }

    // Wrapper to get the list of movies
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Movie>> getMostPopularMovies() {
        return mMoviesMostPopular;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return mMoviesTopRated;
    }

    // TODO NEEDS TO BE DONE OF THE MAIN THREAD
    public void insertMovies(List<Movie> movies) {
        mMovieDao.bulkInsertMovies(movies);
    }

    public void deleteWithOrigin(int origin) {
        mMovieDao.deleteWithOrigin(origin);
    }

    public Movie getMovieById(int id) {
        return mMovieDao.getMovieById(id);
    }

    public boolean isMovieFavorite(int movieId) {
        return mMovieDao.isMovieFavorite(movieId);
    }

    public void insertFavoriteMovie(Movie movie) {
        mMovieDao.insertFavoriteMovie(movie);
    }

    public void deleteFavorite(int movieId) {
        mMovieDao.deleteFavorite(movieId);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mMovieFavorites;
    }
}