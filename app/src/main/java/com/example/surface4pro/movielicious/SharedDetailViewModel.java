/*
 * Copyright (c) 2018. Daniel Penz
 */
package com.example.surface4pro.movielicious;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.model.Review;
import com.example.surface4pro.movielicious.model.Video;

import java.util.List;

/**
 * ViewModel class to share data between the DetailActivity and the Fragments displayed in it.
 */
public class SharedDetailViewModel extends ViewModel {

    // Member variable declaration and instantiation
    private final MutableLiveData<Movie> movieData = new MutableLiveData<>();
    private final MutableLiveData<List<Video>> videoData = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviewData = new MutableLiveData<>();
    private final MutableLiveData<Integer> loadingStatusVideo = new MutableLiveData<>();
    private final MutableLiveData<Integer> loadingStatusReviews = new MutableLiveData<>();

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

    public LiveData<Integer> getLoadingStatusVideo() {
        return loadingStatusVideo;
    }

    public void setLoadingStatusVideo(Integer loadingStatus) {
        loadingStatusVideo.setValue(loadingStatus);
    }

    public LiveData<Integer> getLoadingStatusReviews() {
        return loadingStatusReviews;
    }

    public void setLoadingStatusReviews(Integer loadingStatus) {
        loadingStatusReviews.setValue(loadingStatus);
    }
}