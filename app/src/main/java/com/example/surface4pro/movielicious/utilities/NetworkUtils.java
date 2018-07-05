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

    private final static String MOVIEDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String PATH_IMAGE_WIDTH = "w342";

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
     * Builds the MovieDatabase API Review request URL based on the passed in movieId.
     *
     * @param movieId id of the selected movie
     * @return build URL
     */
    public static URL buildReviewURL(int movieId) {

        /* Building the API request URL */
        Uri builtUri = null;

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
