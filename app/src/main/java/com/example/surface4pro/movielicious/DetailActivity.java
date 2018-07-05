/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.surface4pro.movielicious.data.MovieRoomDatabase;
import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private MovieViewModel mMovieViewModel;

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
            populateUI(movieId);
        }
    }

    /**
     * Populates the UI with the data from the passed in Movie object.
     *
     * @param movieId Movie id.
     */
    private void populateUI(final int movieId) {

        final Movie movie = mMovieViewModel.getMovieById(movieId);

        // Declare and initialize View variables
        ImageView mPosterImageView = findViewById(R.id.iv_poster);
        TextView mTitleTextView = findViewById(R.id.tv_title);
        TextView mYearTextView = findViewById(R.id.tv_year);
        RatingBar mRatingRatingBar = findViewById(R.id.rb_rating);
        TextView mVotesTextView = findViewById(R.id.tv_votes);
        TextView mGenreTextView = findViewById(R.id.tv_genre);
        TextView mDescriptionTextView = findViewById(R.id.tv_description);
        Switch mFavoriteSwitch = findViewById(R.id.sw_favorite);

        // Set the ActionBar Title to the movie's title
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(movie.getTitle());
        }

        // Create the image URL and display it using Picasso
        String url = NetworkUtils.buildImageUrl(movie.getPosterPath());
        Picasso.get().load(url).placeholder(R.drawable.no_poster).into(mPosterImageView);

        // Set the movie detail information to the different views
        mTitleTextView.setText(movie.getTitle());
        mYearTextView.setText(movie.getReleaseDate().substring(0, 4));
        mRatingRatingBar.setRating(movie.getVoteAverage() / 2f);
        mVotesTextView.setText(getResources().getString(R.string.number_of_votes, String.valueOf(movie.getVoteCount())));
        mDescriptionTextView.setText(movie.getDescription());

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

        if (mMovieViewModel.isMovieFavorite(movie.getMovieId())) {
            mFavoriteSwitch.setChecked(true);

        }

        mFavoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    movie.setOrigin(MovieRoomDatabase.ORIGIN_ID_FAVORITES);
                    movie.setId(0);
                    mMovieViewModel.insertFavoriteMovie(movie);
                } else {
                    mMovieViewModel.deleteFavorite(movie.getMovieId());
                }
            }
        });
    }
}
