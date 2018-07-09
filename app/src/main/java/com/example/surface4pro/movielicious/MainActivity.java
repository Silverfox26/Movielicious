/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.surface4pro.movielicious.data.MovieRoomDatabase;
import com.example.surface4pro.movielicious.databinding.ActivityMainBinding;
import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.utilities.AppExecutors;
import com.example.surface4pro.movielicious.utilities.MovieDbJsonUtils;
import com.example.surface4pro.movielicious.utilities.NetworkStatus;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;
import com.facebook.stetho.Stetho;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    // Create a data binding instance called mBinding of type ActivityMainBinding
    ActivityMainBinding mBinding;

    private URL url = null;

    // Member variable declarations
    private List<Movie> movies = null;
    private MovieViewModel mMovieViewModel;

    private Observer<List<Movie>> mObserver;

    private int selection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        // Set the Content View using DataBindingUtil to the activity_main layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Setting the span count for the GridLayoutManager based on the device's orientation
        int value = this.getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager;
        if (value == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 4);
        }

        // Configuring the RecyclerView and setting its adapter
        mBinding.rvMovies.setLayoutManager(layoutManager);
        mBinding.rvMovies.setHasFixedSize(true);
        final MovieAdapter movieAdapter = new MovieAdapter(this);
        mBinding.rvMovies.setAdapter(movieAdapter);

        // The ViewModelProvider will create the ViewModel, when the app first starts.
        // When the activity is destroyed (ex. configuration change), the ViewModel persists.
        // When the activity is recreated, the Provider returns the existing ViewModel.
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        mObserver = new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                // Update the cached copy of the movies in the Adapter.
                movieAdapter.setMovieData(movies);
            }
        };

        if (savedInstanceState != null && savedInstanceState.containsKey("selection")) {
            selection = savedInstanceState.getInt("selection");
        } else if (NetworkStatus.isOnline(this)) {
            deleteMoviesByOrigin(MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR);
            loadMovieData(R.id.menu_most_popular, MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR);
        }

        if (selection == 0) {
            // Observer for the LiveData
            mMovieViewModel.getMostPopularMovies().observe(this, mObserver);
        } else if (selection == 1) {
            mMovieViewModel.getTopRatedMovies().observe(this, mObserver);
        } else if (selection == 2) {
            mMovieViewModel.getFavoriteMovies().observe(this, mObserver);
        }
    }

    private void deleteMoviesByOrigin(int origin) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieViewModel.deleteWithOrigin(origin);
            }
        });
    }

    /**
     * This method sets the RecyclerView invisible and shows the error message
     */
    private void showErrorMessage() {
        mBinding.rvMovies.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showMovieData() {
        mBinding.rvMovies.setVisibility(View.VISIBLE);
        mBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method builds the url and fetches the movie data.
     *
     * @param sortParam integer corresponding to the requested movie data
     *                  (possible values R.id.menu_most_popular and R.id.menu_highest_rated)
     */
    private void loadMovieData(int sortParam, int originId) {
        showMovieData();
        url = NetworkUtils.buildURL(sortParam);
        new FetchMoviesTask(this, originId).execute(url);
    }

    /**
     * Save the movies List in the outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // outState.putParcelableArrayList(getString(R.string.saved_instance_movies), movies);
        outState.putInt("selection", selection);
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
                selection = 0;
                removeObservers();
                mMovieViewModel.getMostPopularMovies().observe(this, mObserver);
                mBinding.rvMovies.smoothScrollToPosition(0);
                if (NetworkStatus.isOnline(this)) {
                    deleteMoviesByOrigin(MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR);
                    loadMovieData(R.id.menu_most_popular, MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR);
                }
                return true;
            case R.id.menu_top_rated:
                selection = 1;
                removeObservers();
                mMovieViewModel.getTopRatedMovies().observe(this, mObserver);
                mBinding.rvMovies.smoothScrollToPosition(0);
                if (NetworkStatus.isOnline(this)) {
                    deleteMoviesByOrigin(MovieRoomDatabase.ORIGIN_ID_TOP_RATED);
                    loadMovieData(R.id.menu_top_rated, MovieRoomDatabase.ORIGIN_ID_TOP_RATED);
                }
                return true;
            case R.id.menu_favorites:
                selection = 2;
                removeObservers();
                mMovieViewModel.getFavoriteMovies().observe(this, mObserver);
                mBinding.rvMovies.smoothScrollToPosition(0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void removeObservers() {
        mMovieViewModel.getMostPopularMovies().removeObserver(mObserver);
        mMovieViewModel.getTopRatedMovies().removeObserver(mObserver);
        mMovieViewModel.getFavoriteMovies().removeObserver(mObserver);
    }

    @Override
    public void onClick(View v, int clickedMovieId, int layoutPosition) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent startDetailActivityIntent = new Intent(context, destinationClass);
        // startDetailActivityIntent.putExtra(getString(R.string.extra_movie), movies.get(layoutPosition));
        startDetailActivityIntent.putExtra(getString(R.string.extra_movie), clickedMovieId);
        Log.d("AAA", "onClick: " + clickedMovieId);

        // Use SceneTransitionAnimation to start DetailActivity
        startActivity(startDetailActivityIntent, ActivityOptions.makeSceneTransitionAnimation(this, v.findViewById(R.id.iv_movie_poster), getString(R.string.transition_poster)).toBundle());

    }

    private static class FetchMoviesTask extends AsyncTask<URL, Void, List<Movie>> {

        private final WeakReference<MainActivity> activityReference;
        private final int originId;

        // only retain a weak reference to the activity
        FetchMoviesTask(MainActivity context, int originId) {
            activityReference = new WeakReference<>(context);
            this.originId = originId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.showMovieData();
            activity.mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(URL... urls) {
            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return null;

            URL queryUrl = urls[0];
            String movieQueryResults;
            try {
                movieQueryResults = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            activity.movies = MovieDbJsonUtils.getMovieDataFromJson(movieQueryResults, originId);

            return activity.movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {

                @Override
                public void run() {
                    activity.mMovieViewModel.insertMovies(movies);
                }
            });

            if (movies != null) {
                activity.showMovieData();
            } else {
                activity.showErrorMessage();
            }
        }
    }
}
