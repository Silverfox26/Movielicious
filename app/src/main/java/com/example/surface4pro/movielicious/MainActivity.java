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
import android.view.Menu;
import android.view.MenuItem;
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

        // TODO - Save Rotation Scroll Position. Convert Genre Numbers to Strings representations.
        mMoviesRecyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, movies);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);


        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            URL url = NetworkUtils.buildURL(R.id.menu_most_popular);
            new FetchMoviesTask().execute(url);
        } else {
            movies = savedInstanceState.getParcelableArrayList("movies");
            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        URL url = null;

        switch (itemThatWasClickedId) {
            case R.id.menu_most_popular:
                url = NetworkUtils.buildURL(R.id.menu_most_popular);
                new FetchMoviesTask().execute(url);
                mMoviesRecyclerView.scrollToPosition(0);
                return true;
            case R.id.menu_top_rated:
                url = NetworkUtils.buildURL(R.id.menu_top_rated);
                new FetchMoviesTask().execute(url);
                mMoviesRecyclerView.scrollToPosition(0);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
            // set an exit transition
            //getWindow().setExitTransition(new Explode());
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
