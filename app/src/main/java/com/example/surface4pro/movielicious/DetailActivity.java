/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.surface4pro.movielicious.data.MovieRoomDatabase;
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

    private MovieViewModel mMovieViewModel;

    private SharedDetailViewModel sharedViewModel;

    private boolean movieIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set exit transition
        getWindow().setEnterTransition(new Explode());

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        // Get the intent, check its content, and populate the UI with its data
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.extra_movie))) {
            int movieId = intent.getIntExtra(getString(R.string.extra_movie), -1);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Movie movie = mMovieViewModel.getMovieById(movieId);
                    movieIsFavorite = mMovieViewModel.isMovieFavorite(movie.getMovieId());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sharedViewModel = ViewModelProviders.of(DetailActivity.this).get(SharedDetailViewModel.class);
                            sharedViewModel.saveMovie(movie);
                            populateUI(movie);
                        }
                    });
                }
            });
        }

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        // Create an adapter that knows which fragment should be shown on each page
        DetailViewFragmentPagerAdapter adapter = new DetailViewFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * Populates the UI with the data from the passed in Movie object.
     *
     * @param movie Movie id.
     */
    private void populateUI(final Movie movie) {

        // Declare and initialize View variables
        ImageView mPosterImageView = findViewById(R.id.iv_movie_poster);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ImageView mBackdropImageView = findViewById(R.id.iv_movie_backdrop);
            String backdropUrl = NetworkUtils.buildImageUrl(movie.getBackdropPath());
            Picasso.get().load(backdropUrl).placeholder(R.drawable.no_poster).into(mBackdropImageView);
        }

        TextView mTitleTextView = findViewById(R.id.tv_title);
        TextView mYearTextView = findViewById(R.id.tv_year);
        RatingBar mRatingRatingBar = findViewById(R.id.rb_rating);
        TextView mVotesTextView = findViewById(R.id.tv_votes);
        TextView mGenreTextView = findViewById(R.id.tv_genre);
        ToggleButton mFavoriteButton = findViewById(R.id.tb_favorite);

        // Create the image URL and display it using Picasso
        String url = NetworkUtils.buildImageUrl(movie.getPosterPath());
        Picasso.get().load(url).placeholder(R.drawable.no_poster).into(mPosterImageView);

        // Set the movie detail information to the different views
        mTitleTextView.setText(movie.getTitle());
        mYearTextView.setText(movie.getReleaseDate().substring(0, 4));
        mRatingRatingBar.setRating(movie.getVoteAverage() / 2f);
        mVotesTextView.setText(getResources().getString(R.string.number_of_votes, String.valueOf(movie.getVoteCount())));

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
        mGenreTextView.setText(genres.toString());

        if (movieIsFavorite) {
            mFavoriteButton.setChecked(true);
            mFavoriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_twotone_favorite_24px));
        }

        mFavoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    movie.setOrigin(MovieRoomDatabase.ORIGIN_ID_FAVORITES);
                    movie.setId(0);
                    mFavoriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_twotone_favorite_24px));

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mMovieViewModel.insertFavoriteMovie(movie);
                        }
                    });

                } else {
                    mFavoriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_twotone_favorite_border_24px));

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mMovieViewModel.deleteFavorite(movie.getMovieId());
                        }
                    });
                }
            }
        });

        Log.d("AAA", "populateUI: " + NetworkUtils.buildReviewURL(movie.getMovieId()));
        URL reviewUrl = NetworkUtils.buildReviewURL(movie.getMovieId());
        new FetchReviewsTask(this).execute(reviewUrl);

        Log.d("AAA", "populateUI: " + NetworkUtils.buildVideoURL(movie.getMovieId()));
        URL videowUrl = NetworkUtils.buildVideoURL(movie.getMovieId());
        new FetchVideosTask(this).execute(videowUrl);
    }


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

            activity.sharedViewModel.setLoadingStatusReviews(1);
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
                activity.sharedViewModel.setLoadingStatusReviews(-1);
            } else if (reviews.isEmpty()) {
                activity.sharedViewModel.setLoadingStatusReviews(0);
            } else {
                activity.sharedViewModel.saveReviewList(reviews);
                activity.sharedViewModel.setLoadingStatusReviews(2);
            }
        }
    }


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

            activity.sharedViewModel.setLoadingStatusVideo(1);
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
                activity.sharedViewModel.setLoadingStatusVideo(-1);
            } else if (videos.isEmpty()) {
                activity.sharedViewModel.setLoadingStatusVideo(0);
            } else {
                activity.sharedViewModel.saveVideoList(videos);
                activity.sharedViewModel.setLoadingStatusVideo(2);
            }
        }
    }
}
