/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;

import com.example.surface4pro.movielicious.data.MovieRoomDatabase;
import com.example.surface4pro.movielicious.databinding.ActivityDetailBinding;
import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.model.Review;
import com.example.surface4pro.movielicious.model.Video;
import com.example.surface4pro.movielicious.utilities.AppExecutors;
import com.example.surface4pro.movielicious.utilities.MovieDbJsonUtils;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    // Constants indicating the loading status
    public static final int LOADING_STATUS_ERROR = -1;
    public static final int LOADING_STATUS_NO_RESULTS_AVAILABLE = 0;
    public static final int LOADING_STATUS_LOADING = 1;
    public static final int LOADING_STATUS_LOADING_SUCCESSFUL = 2;

    // Member variable declarations
    private ActivityDetailBinding mBinding;
    private MovieViewModel mMovieViewModel;
    private SharedDetailViewModel sharedViewModel;
    private boolean movieIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the Content View using DataBindingUtil
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // Set exit transition
        getWindow().setEnterTransition(new Explode());

        // The ViewModelProvider creates the ViewModel, when the app first starts.
        // When the activity is destroyed and recreated, the Provider returns the existing ViewModel.
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        // Get the intent, check its content, and populate the UI with its data
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.extra_movie))) {
            int movieId = intent.getIntExtra(getString(R.string.extra_movie), -1);

            // Retrieve the passed in movie on a background thread from the db using its movieID.
            AppExecutors.getInstance().diskIO().execute(() -> {
                Movie movie = mMovieViewModel.getMovieById(movieId);

                // Check if the movie is already saved as a favorite.
                movieIsFavorite = mMovieViewModel.isMovieFavorite(movie.getMovieId());

                runOnUiThread(() -> {
                    sharedViewModel = ViewModelProviders.of(DetailActivity.this).get(SharedDetailViewModel.class);

                    // Save the Movie in the sharedViewModel, so that it is available
                    // to the Video and Review Fragments.
                    sharedViewModel.saveMovie(movie);

                    populateUI(movie);
                });
            });
        }


        // Create an adapter that knows which fragment should be shown on each page
        DetailViewFragmentPagerAdapter adapter = new DetailViewFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter to the ViewPager
        mBinding.viewPager.setAdapter(adapter);

        // Pass the ViewPager to the TabLayout
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

    }

    /**
     * Populates the UI with the data from the passed in Movie object.
     *
     * @param movie Movie object.
     */
    private void populateUI(final Movie movie) {

        // Check orientation and if Portrait load also the Backdrop image.
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            String backdropUrl = NetworkUtils.buildImageUrl(movie.getBackdropPath());
            Picasso.get().load(backdropUrl).placeholder(R.drawable.no_poster).into(mBinding.ivMovieBackdrop);
        }

        // Create the image URL and display it using Picasso
        String url = NetworkUtils.buildImageUrl(movie.getPosterPath());
        Picasso.get().load(url).placeholder(R.drawable.no_poster).into(mBinding.ivMoviePoster);

        // Set the movie detail information to the different views
        mBinding.tvTitle.setText(movie.getTitle());
        mBinding.tvYear.setText(movie.getReleaseDate().substring(0, 4));
        mBinding.rbRating.setRating(movie.getVoteAverage() / 2f);
        mBinding.tvVotes.setText(getResources().getString(R.string.number_of_votes, String.valueOf(movie.getVoteCount())));

        // Convert the genre id's to their corresponding names and
        // concatenate them using a StringBuilder
        StringBuilder genres = new StringBuilder();
        int[] genreArray = movie.getGenreIds();

        int index = 0;
        for (int genre : genreArray) {
            switch (genre) {
                case 28:
                    genres.append(getString(R.string.genre_action));
                    break;
                case 12:
                    genres.append(getString(R.string.genre_adventure));
                    break;
                case 16:
                    genres.append(getString(R.string.genre_animation));
                    break;
                case 35:
                    genres.append(getString(R.string.genre_comedy));
                    break;
                case 80:
                    genres.append(getString(R.string.genre_crime));
                    break;
                case 99:
                    genres.append(getString(R.string.genre_documentary));
                    break;
                case 18:
                    genres.append(getString(R.string.genre_drama));
                    break;
                case 10751:
                    genres.append(getString(R.string.genre_family));
                    break;
                case 14:
                    genres.append(getString(R.string.genre_fantasy));
                    break;
                case 36:
                    genres.append(getString(R.string.genre_history));
                    break;
                case 27:
                    genres.append(getString(R.string.genre_horror));
                    break;
                case 10402:
                    genres.append(getString(R.string.genre_music));
                    break;
                case 9648:
                    genres.append(getString(R.string.genre_mystery));
                    break;
                case 10749:
                    genres.append(getString(R.string.genre_romance));
                    break;
                case 878:
                    genres.append(getString(R.string.genre_science_fiction));
                    break;
                case 10770:
                    genres.append(getString(R.string.genre_tv_movie));
                    break;
                case 53:
                    genres.append(getString(R.string.genre_thriller));
                    break;
                case 10752:
                    genres.append(getString(R.string.genre_war));
                    break;
                case 37:
                    genres.append(getString(R.string.genre_western));
                    break;
            }
            if (index < genreArray.length - 1) {
                genres.append(", ");
            }
            index++;
        }
        mBinding.tvGenre.setText(genres.toString());

        // If the movie is already a favorite, set the favorite button accordingly
        if (movieIsFavorite) {
            mBinding.tbFavorite.setChecked(true);
            mBinding.tbFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_twotone_favorite_24px));
        }

        // Listen to changes of the favorite button
        mBinding.tbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // If movie is made favorite, set its favorite flag and clear its id.
                movie.setOrigin(MovieRoomDatabase.ORIGIN_ID_FAVORITES);
                movie.setId(0);

                mBinding.tbFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_twotone_favorite_24px));

                // Add the favorite movie to the database
                AppExecutors.getInstance().diskIO().execute(() -> mMovieViewModel.insertFavoriteMovie(movie));

            } else {
                mBinding.tbFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_twotone_favorite_border_24px));

                // Delete the favorite movie from the database
                AppExecutors.getInstance().diskIO().execute(() -> mMovieViewModel.deleteFavorite(movie.getMovieId()));
            }
        });

        // Build the URL to retrieve the movie's reviews and load them using AsyncTask
        URL reviewUrl = NetworkUtils.buildReviewURL(movie.getMovieId());
        new FetchReviewsTask(this).execute(reviewUrl);

        // Build the URL to retrieve the movie's videos and load them using AsyncTask
        URL videoUrl = NetworkUtils.buildVideoURL(movie.getMovieId());
        new FetchVideosTask(this).execute(videoUrl);
    }

    /**
     * AsyncTask to retrieve the movie's reviews and save them in the SharedViewModel
     */
    private static class FetchReviewsTask extends AsyncTask<URL, Void, List<Review>> {

        private final WeakReference<DetailActivity> activityReference;

        // only retain a weak reference to the activity
        FetchReviewsTask(DetailActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // get a reference to the activity if it is still there
            DetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.sharedViewModel.setLoadingStatusReviews(LOADING_STATUS_LOADING);
        }

        @Override
        protected List<Review> doInBackground(URL... urls) {
            // get a reference to the activity if it is still there
            DetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return null;

            URL queryUrl = urls[0];
            String reviewQueryResults;
            try {
                reviewQueryResults = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return MovieDbJsonUtils.getReviewDataFromJson(reviewQueryResults);
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            // get a reference to the activity if it is still there
            DetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            if (reviews == null) {
                activity.sharedViewModel.setLoadingStatusReviews(LOADING_STATUS_ERROR);
            } else if (reviews.isEmpty()) {
                activity.sharedViewModel.setLoadingStatusReviews(LOADING_STATUS_NO_RESULTS_AVAILABLE);
            } else {
                activity.sharedViewModel.saveReviewList(reviews);
                activity.sharedViewModel.setLoadingStatusReviews(LOADING_STATUS_LOADING_SUCCESSFUL);
            }
        }
    }

    /**
     * AsyncTask to retrieve the movie's videos and save them in the SharedViewModel
     */
    private static class FetchVideosTask extends AsyncTask<URL, Void, List<Video>> {

        private final WeakReference<DetailActivity> activityReference;

        // only retain a weak reference to the activity
        FetchVideosTask(DetailActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // get a reference to the activity if it is still there
            DetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.sharedViewModel.setLoadingStatusVideo(LOADING_STATUS_LOADING);
        }

        @Override
        protected List<Video> doInBackground(URL... urls) {
            // get a reference to the activity if it is still there
            DetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return null;

            URL queryUrl = urls[0];
            String videoQueryResults;
            try {
                videoQueryResults = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return MovieDbJsonUtils.getVideoDataFromJson(videoQueryResults);
        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            // get a reference to the activity if it is still there
            DetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            if (videos == null) {
                activity.sharedViewModel.setLoadingStatusVideo(LOADING_STATUS_ERROR);
            } else if (videos.isEmpty()) {
                activity.sharedViewModel.setLoadingStatusVideo(LOADING_STATUS_NO_RESULTS_AVAILABLE);
            } else {
                activity.sharedViewModel.saveVideoList(videos);
                activity.sharedViewModel.setLoadingStatusVideo(LOADING_STATUS_LOADING_SUCCESSFUL);
            }
        }
    }
}