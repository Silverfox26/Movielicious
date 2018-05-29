package com.example.surface4pro.movielicious;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.surface4pro.movielicious.utilities.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new FetchMoviesTask().execute(url);

    }

    public class FetchMoviesTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL queryUrl = urls[0];
            String movieQueryResults = null;
            try {
                movieQueryResults = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return movieQueryResults;
        }

        @Override
        protected void onPostExecute(String movieQueryResults) {
            if (movieQueryResults != null && !movieQueryResults.equals("")) {
                Log.d("HTTP Result: ", movieQueryResults);
            }
        }
    }
}
