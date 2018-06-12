package com.example.surface4pro.movielicious;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.utilities.MovieDbJsonUtils;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private ArrayList<Movie> movies = null;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mMoviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO - Create Detail Activity and fill in the data. Save Rotation Scroll Position.
        mMoviesRecyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, movies);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        URL url = NetworkUtils.buildURL();
        new FetchMoviesTask().execute(url);
    }

    @Override
    public void onClick(View v, String clickedMovie, int layoutPosition) {
        Log.d("CLICK", clickedMovie);

        Context context = this;
        Class destinationClass = DetailActivity.class;


        Intent startDetailActivityIntent = new Intent(context, destinationClass);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("movies", movies);
        bundle.putInt("clickedMovie", layoutPosition);
        startDetailActivityIntent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(startDetailActivityIntent, ActivityOptions.makeSceneTransitionAnimation(this, v.findViewById(R.id.iv_movie_poster), "transition_poster").toBundle());
        }
    }


    public class FetchMoviesTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
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

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
            }
        }
    }
}
