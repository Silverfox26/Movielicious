/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious.utilities;

import android.net.Uri;

import com.example.surface4pro.movielicious.BuildConfig;
import com.example.surface4pro.movielicious.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utilities to communicate with the movie database servers
 */
public final class NetworkUtils {

    // Defining URL constants
    private final static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3";
    private final static String PATH_MOVIES = "movie";
    private final static String PATH_SORT_POPULAR = "popular";
    private final static String PATH_SORT_TOP_RATED = "top_rated";
    private final static String PARAM_QUERY = "api_key";
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    // URL constants for reviews
    private final static String PATH_REVIEWS = "reviews";
    private final static String PATH_VIDEOS = "videos";

    private final static String MOVIEDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String PATH_IMAGE_WIDTH = "w342";

    private final static String YOUTUBE_PREVIEW_IMAGE_BASE_URL = "https://img.youtube.com/vi";
    private final static String YOUTUBE_PREVIEW_IMAGE_FILE = "0.jpg";

    private final static String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch";
    private final static String YOUTUBE_VIDEO_PARAM_QUERY = "v";

    private final static String YOUTUBE_VIDEO_APP_BASE_URL = "vnd.youtube:";

    /**
     * Builds the MovieDatabase API request URL based in the passe in sort parameter.
     *
     * @param menuSelection id of the selected menu item
     * @return build URL
     */
    public static URL buildURL(int menuSelection) {

        /* Building the API request URL */
        Uri builtUri = null;

        switch (menuSelection) {
            case R.id.menu_most_popular:
                builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendPath(PATH_MOVIES)
                        .appendPath(PATH_SORT_POPULAR)
                        .appendQueryParameter(PARAM_QUERY, API_KEY)
                        .build();
                break;
            case R.id.menu_top_rated:
                builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendPath(PATH_MOVIES)
                        .appendPath(PATH_SORT_TOP_RATED)
                        .appendQueryParameter(PARAM_QUERY, API_KEY)
                        .build();
                break;
        }

        URL url = null;
        try {
            url = new URL(builtUri != null ? builtUri.toString() : null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the MovieDatabase API review request URL based on the passed in movieId.
     *
     * @param movieId id of the selected movie
     * @return build URL
     */
    public static URL buildReviewURL(int movieId) {

        /* Building the API request URL */
        Uri builtUri;

        builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIES)
                .appendPath(String.valueOf(movieId))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri != null ? builtUri.toString() : null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the MovieDatabase API video request URL based on the passed in movieId.
     *
     * @param movieId id of the selected movie
     * @return build URL
     */
    public static URL buildVideoURL(int movieId) {

        /* Building the API request URL */
        Uri builtUri;

        builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIES)
                .appendPath(String.valueOf(movieId))
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri != null ? builtUri.toString() : null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the movie poster URL.
     *
     * @param movieImagePath the path to the movie poster
     * @return build mage URL
     */
    public static String buildImageUrl(String movieImagePath) {
        Uri builtUri = Uri.parse(MOVIEDB_IMAGE_BASE_URL).buildUpon()
                .appendPath(PATH_IMAGE_WIDTH)
                .appendEncodedPath(movieImagePath)
                .build();

        return builtUri.toString();
    }

    /**
     * Builds the video preview image URL.
     *
     * @param videoKey the video's key
     * @return build mage URL
     */
    public static String buildVideoImageUrl(String videoKey) {
        Uri builtUri = Uri.parse(YOUTUBE_PREVIEW_IMAGE_BASE_URL).buildUpon()
                .appendPath(videoKey)
                .appendEncodedPath(YOUTUBE_PREVIEW_IMAGE_FILE)
                .build();

        return builtUri.toString();
    }

    /**
     * Builds the youtube video http URI.
     *
     * @param videoId id of the selected video
     * @return build URI
     */
    public static Uri buildYouTubeVideoURI(String videoId) {

        /* Building the API request URL */
        Uri builtUri;

        builtUri = Uri.parse(YOUTUBE_VIDEO_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_VIDEO_PARAM_QUERY, videoId)
                .build();

        return builtUri;
    }

    /**
     * Builds the youtube video app URI.
     *
     * @param videoId id of the selected movie
     * @return build URI
     */
    public static Uri buildYouTubeAppVideoURI(String videoId) {

        /* Building the API request URL */

        return Uri.parse(YOUTUBE_VIDEO_APP_BASE_URL + videoId);
    }

    /**
     * Method taken from the Udacity Android Developer Nanodegree Sunshine Project.
     * <p>
     * Method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from
     * @return The contents of the HTTP response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}