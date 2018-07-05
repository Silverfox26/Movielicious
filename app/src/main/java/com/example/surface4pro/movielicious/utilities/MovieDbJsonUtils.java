/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious.utilities;

import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class MovieDbJsonUtils {

    /**
     * Parses a MovieDatabase JSON String into an ArrayList of Movie objects.
     *
     * @param movieJsonString a Json String received from TheMovieDatabase
     * @return an ArrayList consisting of Movie objects parsed from the movieJsonString
     */
    public static ArrayList<Movie> getMovieDataFromJson(String movieJsonString, int originId) {
        ArrayList<Movie> movieList = new ArrayList<>();

        // String Constants for parsing the Json String
        final String RESULTS = "results";
        final String ID = "id";
        final String TITLE = "title";
        final String ORIGINAL_TITLE = "original_title";
        final String VOTE_COUNT = "vote_count";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String OVERVIEW = "overview";
        final String POPULARITY = "popularity";
        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path";
        final String VIDEO = "video";
        final String ORIGINAL_LANGUAGE = "original_language";
        final String GENRE_IDS = "genre_ids";
        final String ADULT = "adult";


        JSONObject movies;
        try {
            movies = new JSONObject(movieJsonString);

            JSONArray results = movies.getJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);

                int id = movie.getInt(ID);
                String title = movie.getString(TITLE);
                String originalTitle = movie.getString(ORIGINAL_TITLE);
                int voteCount = movie.getInt(VOTE_COUNT);
                int voteAverage = movie.getInt(VOTE_AVERAGE);
                String releaseDate = movie.getString(RELEASE_DATE);
                String description = movie.getString(OVERVIEW);
                int popularity = movie.getInt(POPULARITY);
                String posterPath = movie.getString(POSTER_PATH);
                String backdropPath = movie.getString(BACKDROP_PATH);
                boolean video = movie.getBoolean(VIDEO);
                String originalLanguage = movie.getString(ORIGINAL_LANGUAGE);

                JSONArray genreIdsArray = movie.getJSONArray(GENRE_IDS);
                int[] genreIds = new int[genreIdsArray.length()];

                for (int j = 0; j < genreIdsArray.length(); j++) {
                    genreIds[j] = genreIdsArray.getInt(j);
                }
                boolean onlyForAdults = movie.getBoolean(ADULT);

                movieList.add(i, new Movie(
                        id,
                        title,
                        originalTitle,
                        voteCount,
                        voteAverage,
                        releaseDate,
                        description,
                        popularity,
                        posterPath,
                        backdropPath,
                        video,
                        originalLanguage,
                        genreIds,
                        onlyForAdults,
                        originId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return movieList;
    }


    public static ArrayList<Review> getReviewDataFromJson(String reviewJsonString) {
        ArrayList<Review> reviewList = new ArrayList<>();

        // String Constants for parsing the Json String
        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        JSONObject reviews;
        try {
            reviews = new JSONObject(reviewJsonString);

            JSONArray results = reviews.getJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject review = results.getJSONObject(i);

                String author = review.getString(AUTHOR);
                String content = review.getString(CONTENT);

                reviewList.add(i, new Review(
                        author,
                        content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return reviewList;
    }

}