package com.example.surface4pro.movielicious.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.surface4pro.movielicious.model.Movie;

import java.util.List;

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mMovies;

    public MovieRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        // TODO Change to variable Origins
        mMovies = mMovieDao.getMoviesByOrigin(0);
    }

    // Wrapper to get the list of movies
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    // TODO NEEDS TO BE DONE OF THE MAIN THREAD
    public void insertMovies(List<Movie> movies) {
        mMovieDao.bulkInsertMovies(movies);
    }
}
