package com.example.surface4pro.movielicious;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.utilities.MovieDbJsonUtils;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Movie> movies = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL url = NetworkUtils.buildURL();
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

            movies = MovieDbJsonUtils.getMovieDataFromJson(movieQueryResults);
            if (movies != null) {
                Log.d("MObject", movies.get(1).getOriginalTitle());
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
