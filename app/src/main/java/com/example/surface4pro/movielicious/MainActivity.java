package com.example.surface4pro.movielicious;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.utilities.MovieDbJsonUtils;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    URL url = null;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mMoviesRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    // Member variable declarations
    private ArrayList<Movie> movies = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO - Add/finish Documentation
        // Initializing the View variables
        mMoviesRecyclerView = findViewById(R.id.rv_movies);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.tv_error_message);

        // Setting the span count for the GridLayoutManager based on the device's orientation
        int value = this.getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager = null;
        if (value == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 4);
        }

        // Configuring the RecyclerView and setting its adapter
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, movies);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        // Fetching the movie data from the Internet
        // or using the local data in savedInstanceState, if available
        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            loadMovieData(R.id.menu_most_popular);
        } else {
            movies = savedInstanceState.getParcelableArrayList("movies");
            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
            }
        }
    }

    /**
     * This method sets the RecyclerView invisible and shows the error message
     */
    public void showErrorMessage() {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    public void showMovieData() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method builds the url and fetches the movie data.
     *
     * @param sortParam integer corresponding to the requested movie data
     *                  (possible values R.id.menu_most_popular and R.id.menu_highest_rated)
     */
    private void loadMovieData(int sortParam) {
        showMovieData();
        url = NetworkUtils.buildURL(sortParam);
        new FetchMoviesTask().execute(url);
    }

    /**
     * Save the movies ArrayList in the outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        url = null;

        switch (itemThatWasClickedId) {
            case R.id.menu_most_popular:
                loadMovieData(R.id.menu_most_popular);
                return true;
            case R.id.menu_top_rated:
                loadMovieData(R.id.menu_top_rated);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v, String clickedMovie, int layoutPosition) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent startDetailActivityIntent = new Intent(context, destinationClass);
        startDetailActivityIntent.putExtra("movie", movies.get(layoutPosition));

        // Use SceneTransitionAnimation if SDK Version is high enough
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(startDetailActivityIntent, ActivityOptions.makeSceneTransitionAnimation(this, v.findViewById(R.id.iv_movie_poster), "transition_poster").toBundle());
        } else {
            startActivity(startDetailActivityIntent);
        }
    }

    public class FetchMoviesTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showMovieData();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

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

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMovieData();
                mMovieAdapter.setMovieData(movies);
                mMoviesRecyclerView.scrollToPosition(0);
            } else {
                showErrorMessage();
            }
        }
    }
}
