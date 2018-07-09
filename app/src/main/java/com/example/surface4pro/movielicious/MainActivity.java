/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final int MENU_SELECTION_MOST_POPULAR = 0;
    private static final int MENU_SELECTION_TOP_RATED = 1;
    private static final int MENU_SELECTION_FAVORITES = 2;
    // Member variable declarations
    private ActivityMainBinding mBinding;
    private List<Movie> mMovies = null;
    private URL mUrl = null;
    private MovieViewModel mMovieViewModel;
    private MovieAdapter mMovieAdapter;
    private Observer<List<Movie>> mObserver;
    private int mMenuSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the Content View using DataBindingUtil
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
        mMovieAdapter = new MovieAdapter(this);
        mBinding.rvMovies.setAdapter(mMovieAdapter);

        // The ViewModelProvider creates the ViewModel, when the app first starts.
        // When the activity is destroyed and recreated, the Provider returns the existing ViewModel.
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        // Update the cached copy of the movies in the Adapter.
        mObserver = mMovieAdapter::setMovieData;

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.MENU_SELECTION_INSTANCE_KEY))) {
            mMenuSelection = savedInstanceState.getInt(getString(R.string.MENU_SELECTION_INSTANCE_KEY));
        } else if (NetworkStatus.isOnline(this)) {
            loadMovieData(R.id.menu_most_popular, MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR);
        }

        checkDatabaseAndSetViews(mMenuSelection);

        // Set the observer for the LiveData depending on the selected menu item.
        switch (mMenuSelection) {
            case MENU_SELECTION_MOST_POPULAR:
                mMovieViewModel.getMostPopularMovies().observe(this, mObserver);
                break;
            case MENU_SELECTION_TOP_RATED:
                mMovieViewModel.getTopRatedMovies().observe(this, mObserver);
                break;
            case MENU_SELECTION_FAVORITES:
                mMovieViewModel.getFavoriteMovies().observe(this, mObserver);
                break;
        }
    }

    /**
     * This method uses a background thread to delete all movies with the same origin flag.
     *
     * @param origin Id for the movies origin. Possible values:
     *               - MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR
     *               - MovieRoomDatabase.ORIGIN_ID_TOP_RATED
     *               - MovieRoomDatabase.ORIGIN_ID_FAVORITES
     */
    private void deleteMoviesByOrigin(int origin) {
        AppExecutors.getInstance().diskIO().execute(() -> mMovieViewModel.deleteWithOrigin(origin));
    }

    /**
     * This method sets the RecyclerView and the no favorites message invisible
     * and shows the error message
     */
    private void showErrorMessage() {
        mBinding.rvMovies.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessage.setVisibility(View.VISIBLE);
        mBinding.tvNoFavorites.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView
     * and hides the error message and the no favorites message
     */
    private void showMovieData() {
        mBinding.rvMovies.setVisibility(View.VISIBLE);
        mBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
        mBinding.tvNoFavorites.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the no favorites message
     * and hides the error message and the RecyclerView
     */
    private void showNoFavorites() {
        mBinding.rvMovies.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
        mBinding.tvNoFavorites.setVisibility(View.VISIBLE);
    }

    /**
     * This method builds the url and fetches the movie data.
     *
     * @param sortParam integer corresponding to the requested movie data
     *                  (possible values R.id.menu_most_popular and R.id.menu_highest_rated)
     */
    private void loadMovieData(int sortParam, int originId) {
        showMovieData();
        mUrl = NetworkUtils.buildURL(sortParam);
        new FetchMoviesTask(this, originId).execute(mUrl);
    }

    /**
     * Checks if movies with a certain origin exost in the database and sets the views accordingly
     *
     * @param origin Origin to check for
     */
    private void checkDatabaseAndSetViews(int origin) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            // Check if the movie is already saved as a favorite.
            boolean originExists = mMovieViewModel.doesOriginExist(origin);
            runOnUiThread(() -> {
                if (originExists) {
                    showMovieData();
                } else if (origin == MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR
                        || origin == MovieRoomDatabase.ORIGIN_ID_TOP_RATED) {
                    showErrorMessage();
                } else if (origin == MovieRoomDatabase.ORIGIN_ID_FAVORITES) {
                    showNoFavorites();
                }
            });
        });
    }

    /**
     * Save the menu selection in the outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("selection", mMenuSelection);
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
        mUrl = null;

        switch (itemThatWasClickedId) {
            case R.id.menu_most_popular:
                // Remove any existing observers and set a new observer for the most popular movies.
                removeObservers();
                mMenuSelection = MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR;
                mMovieViewModel.getMostPopularMovies().observe(this, mObserver);
                mBinding.rvMovies.smoothScrollToPosition(0);

                if (NetworkStatus.isOnline(this)) {
                    loadMovieData(R.id.menu_most_popular, MovieRoomDatabase.ORIGIN_ID_MOST_POPULAR);
                } else {
                    checkDatabaseAndSetViews(mMenuSelection);
                }
                return true;
            case R.id.menu_top_rated:
                // Remove any existing observers and set a new observer for the top rated movies.
                removeObservers();
                mMenuSelection = MovieRoomDatabase.ORIGIN_ID_TOP_RATED;
                mMovieViewModel.getTopRatedMovies().observe(this, mObserver);
                mBinding.rvMovies.smoothScrollToPosition(0);

                if (NetworkStatus.isOnline(this)) {
                    loadMovieData(R.id.menu_top_rated, MovieRoomDatabase.ORIGIN_ID_TOP_RATED);
                } else {
                    checkDatabaseAndSetViews(mMenuSelection);
                }
                return true;
            case R.id.menu_favorites:
                // Remove any existing observers and set a new observer for the favorite movies.
                removeObservers();
                mMenuSelection = MovieRoomDatabase.ORIGIN_ID_FAVORITES;
                mMovieViewModel.getFavoriteMovies().observe(this, mObserver);
                mBinding.rvMovies.smoothScrollToPosition(0);
                checkDatabaseAndSetViews(mMenuSelection);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method removes all observers
     */
    private void removeObservers() {
        mMovieViewModel.getMostPopularMovies().removeObserver(mObserver);
        mMovieViewModel.getTopRatedMovies().removeObserver(mObserver);
        mMovieViewModel.getFavoriteMovies().removeObserver(mObserver);
    }

    @Override
    public void onClick(View v, int clickedMovieId) {
        // Create an intent with the clicked on MovieID
        Intent startDetailActivityIntent = new Intent(this, DetailActivity.class);
        startDetailActivityIntent.putExtra(getString(R.string.extra_movie), clickedMovieId);

        // Use SceneTransitionAnimation to start DetailActivity
        startActivity(startDetailActivityIntent, ActivityOptions.makeSceneTransitionAnimation(this, v.findViewById(R.id.iv_movie_poster), getString(R.string.transition_poster)).toBundle());

    }

    /**
     * AsyncTask to fetch to load a list of movies from the movie db API.
     */
    private static class FetchMoviesTask extends AsyncTask<URL, Void, List<Movie>> {

        private final WeakReference<MainActivity> activityReference;
        private final int originId;

        FetchMoviesTask(MainActivity context, int originId) {
            // only retain a weak reference to the activity
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

            activity.mMovies = MovieDbJsonUtils.getMovieDataFromJson(movieQueryResults, originId);

            return activity.mMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movies != null) {
                activity.deleteMoviesByOrigin(originId);
                AppExecutors.getInstance().diskIO().execute(() -> activity.mMovieViewModel.insertMovies(movies));
                activity.showMovieData();
            } else {
                activity.showErrorMessage();
            }
        }
    }
}