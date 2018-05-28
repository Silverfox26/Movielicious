package com.example.surface4pro.movielicious;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // Defining URL constants
    final static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3";
    final static String PATH_MOVIES = "movie";
    final static String PATH_SORT_POPULAR = "popular";
    final static String PATH_SORT_TOP_RATED = "top_rated";
    final static String PARAM_QUERY = "api_key";
    final static String API_KEY = "YOUR_API_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Building the API request URL */

        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIES)
                .appendPath(PATH_SORT_POPULAR)
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();
        Log.d("URI: ", builtUri.toString());
    }
}
