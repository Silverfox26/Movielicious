package com.example.surface4pro.movielicious.utilities;

import com.example.surface4pro.movielicious.model.Movie;

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
    public static ArrayList<Movie> getMovieDataFromJson(String movieJsonString) {
        ArrayList<Movie> movieList = new ArrayList<Movie>();

        JSONObject movies = null;
        try {
            movies = new JSONObject(movieJsonString);

            JSONArray results = movies.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);

                int id = movie.getInt("id");
                String title = movie.getString("title");
                String originalTitle = movie.getString("original_title");
                int voteCount = movie.getInt("vote_count");
                int voteAverage = movie.getInt("vote_average");
                String releaseDate = movie.getString("release_date");
                String description = movie.getString("overview");
                int popularity = movie.getInt("popularity");
                String posterPath = movie.getString("poster_path");
                String backdropPath = movie.getString("backdrop_path");
                boolean video = movie.getBoolean("video");
                String originalLanguage = movie.getString("original_language");

                JSONArray genreIdsArray = movie.getJSONArray("genre_ids");
                int[] genreIds = new int[genreIdsArray.length()];

                for (int j = 0; j < genreIdsArray.length(); j++) {
                    genreIds[j] = genreIdsArray.getInt(j);
                }
                boolean onlyForAdults = movie.getBoolean("adult");

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
                        onlyForAdults));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return movieList;
    }
}