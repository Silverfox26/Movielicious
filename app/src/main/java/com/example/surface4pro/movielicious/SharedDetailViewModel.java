package com.example.surface4pro.movielicious;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.surface4pro.movielicious.model.Movie;

public class SharedDetailViewModel extends ViewModel {
    private final MutableLiveData<Movie> selected = new MutableLiveData<Movie>();

    public void select(Movie movie) {
        selected.setValue(movie);
    }

    public LiveData<Movie> getSelected() {
        return selected;
    }
}
