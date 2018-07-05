package com.example.surface4pro.movielicious;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.model.Review;
import com.example.surface4pro.movielicious.model.Video;

import java.util.List;

public class SharedDetailViewModel extends ViewModel {
    private final MutableLiveData<Movie> movieData = new MutableLiveData<Movie>();
    private final MutableLiveData<List<Video>> videoData = new MutableLiveData<List<Video>>();
    private final MutableLiveData<List<Review>> reviewData = new MutableLiveData<List<Review>>();

    public void saveMovie(Movie movie) {
        movieData.setValue(movie);
    }

    public LiveData<Movie> getSavedMovie() {
        return movieData;
    }

    public void saveVideoList(List<Video> videoList) {
        videoData.setValue(videoList);
    }

    public LiveData<List<Video>> getSavedVideoList() {
        return videoData;
    }

    public void saveReviewList(List<Review> reviewList) {
        reviewData.setValue(reviewList);
    }

    public LiveData<List<Review>> getSavedReviewList() {
        return reviewData;
    }
}
