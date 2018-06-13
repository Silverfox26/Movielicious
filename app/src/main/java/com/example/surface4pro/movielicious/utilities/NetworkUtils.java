package com.example.surface4pro.movielicious.utilities;

import android.net.Uri;

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
    final static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3";
    final static String PATH_MOVIES = "movie";
    final static String PATH_SORT_POPULAR = "popular";
    final static String PATH_SORT_TOP_RATED = "top_rated";
    final static String PARAM_QUERY = "api_key";
    final static String API_KEY = "YOUR_API_KEY";

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

    /**
     * Builds the movie poster URL.
     *
     * @param movieImagePath the path to the movie poster
     * @return build mage URL
     */
    public static String buildImageUrl(String movieImagePath) {
        Uri builtUri = Uri.parse("http://image.tmdb.org/t/p/").buildUpon()
                .appendPath("w185")
                .appendEncodedPath(movieImagePath)
                .build();

        String url = null;
        return url = builtUri.toString();
    }
}
