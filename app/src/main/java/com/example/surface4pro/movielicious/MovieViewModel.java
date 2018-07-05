package com.example.surface4pro.movielicious;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.surface4pro.movielicious.data.MovieRepository;
import com.example.surface4pro.movielicious.model.Movie;

import java.util.ArrayList;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;
    private LiveData<ArrayList<Movie>> mMovies;

    public MovieViewModel(Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        mMovies = mRepository.getMovies();
    }

    // Getter Method to completely hide the implementation from the UI
    public LiveData<ArrayList<Movie>> getMovies() {
        return mMovies;
    }
    
    public void insertMovies(Movie... movies) {
        mRepository.insertMovies(movies);
    }
}
