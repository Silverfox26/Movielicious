package com.example.surface4pro.movielicious;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.surface4pro.movielicious.data.MovieRepository;
import com.example.surface4pro.movielicious.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;
    private LiveData<List<Movie>> mMovies;

    public MovieViewModel(Application application) {
        super(application);
        mRepository = new MovieRepository(application);
    }

    // Getter Method to completely hide the implementation from the UI
    public LiveData<List<Movie>> getMostPopularMovies() {
        mMovies = mRepository.getMostPopularMovies();
        return mMovies;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        mMovies = mRepository.getTopRatedMovies();
        return mMovies;
    }

    public void insertMovies(List<Movie> movies) {
        mRepository.insertMovies(movies);
    }

    public void deleteWithOrigin(int origin) {
        mRepository.deleteWithOrigin(origin);
    }

    public Movie getMovieById(int id) {
        return mRepository.getMovieById(id);
    }

    public boolean isMovieFavorite(int movieId) {
        return mRepository.isMovieFavorite(movieId);
    }

    public void insertFavoriteMovie(Movie movie) {
        mRepository.insertFavoriteMovie(movie);
    }

    public void deleteFavorite(int movieId) {
        mRepository.deleteFavorite(movieId);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mRepository.getFavoriteMovies();
    }
}
