package com.example.surface4pro.movielicious;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    // Member variable declarations
    private Movie mMovie;

    private ImageView mPosterImageView;
    private TextView mTitelTextView;
    private TextView mYearTextVies;
    private RatingBar mRatingRatingBar;
    private TextView mVotesTextView;
    private TextView mGenreTextView;
    private TextView mDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set an exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }

        // Initialize the View variables
        mPosterImageView = findViewById(R.id.iv_poster);
        mTitelTextView = findViewById(R.id.tv_title);
        mYearTextVies = findViewById(R.id.tv_year);
        mRatingRatingBar = findViewById(R.id.rb_rating);
        mVotesTextView = findViewById(R.id.tv_votes);
        mGenreTextView = findViewById(R.id.tv_genre);
        mDescriptionTextView = findViewById(R.id.tv_description);

        // Get the intent and check its content
        Intent intent = getIntent();
        if (intent.hasExtra("movie")) {
            mMovie = intent.getParcelableExtra("movie");

            // Create the image URL and display it using Picasso
            String url = NetworkUtils.buildImageUrl(mMovie.getPosterPath());
            Picasso.get().load(url).into(mPosterImageView);

            // Set the movie detail information to the different views
            mTitelTextView.setText(mMovie.getTitle());
            mYearTextVies.setText(mMovie.getReleaseDate().substring(0, 4));
            mRatingRatingBar.setRating(mMovie.getVoteAverage() / 2f);
            mVotesTextView.setText("(" + String.valueOf(mMovie.getVoteCount()) + ")");
            mDescriptionTextView.setText(mMovie.getDescription());

            // Convert the genre id's to their corresponding names and
            // concatenate them using a StringBuilder
            StringBuilder genres = new StringBuilder();
            int[] genreArray = mMovie.getGenreIds();

            int index = 0;
            for (int genre : genreArray) {
                switch (genre) {
                    case 28:
                        genres.append("Action");
                        break;
                    case 12:
                        genres.append("Adventure");
                        break;
                    case 16:
                        genres.append("Animation");
                        break;
                    case 35:
                        genres.append("Comedy");
                        break;
                    case 80:
                        genres.append("Crime");
                        break;
                    case 99:
                        genres.append("Documentary");
                        break;
                    case 18:
                        genres.append("Drama");
                        break;
                    case 10751:
                        genres.append("Family");
                        break;
                    case 14:
                        genres.append("Fantasy");
                        break;
                    case 36:
                        genres.append("History");
                        break;
                    case 27:
                        genres.append("Horror");
                        break;
                    case 10402:
                        genres.append("Music");
                        break;
                    case 9648:
                        genres.append("Mystery");
                        break;
                    case 10749:
                        genres.append("Romance");
                        break;
                    case 878:
                        genres.append("Science Fiction");
                        break;
                    case 10770:
                        genres.append("TV Movie");
                        break;
                    case 53:
                        genres.append("Thriller");
                        break;
                    case 10752:
                        genres.append("War");
                        break;
                    case 37:
                        genres.append("Western");
                        break;
                }
                if (index < genreArray.length - 1) {
                    genres.append(", ");
                }
                index++;
            }

            mGenreTextView.setText(genres.toString());

            // Set the ActionBar Title to the movie's title
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setTitle(mMovie.getTitle());
            }
        }
    }
}
