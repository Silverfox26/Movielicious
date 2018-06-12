package com.example.surface4pro.movielicious;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface4pro.movielicious.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;
    private ArrayList<Movie> mMovies;

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

        mPosterImageView = findViewById(R.id.iv_poster);
        mTitelTextView = findViewById(R.id.tv_title);
        mYearTextVies = findViewById(R.id.tv_year);
        mRatingRatingBar = findViewById(R.id.rb_rating);
        mVotesTextView = findViewById(R.id.tv_votes);
        mGenreTextView = findViewById(R.id.tv_genre);
        mDescriptionTextView = findViewById(R.id.tv_description);

        Bundle bundleObject = getIntent().getExtras();

        if (bundleObject != null && bundleObject.containsKey("movies") && bundleObject.containsKey("clickedMovie")) {
            mMovies = bundleObject.getParcelableArrayList("movies");
            int id = bundleObject.getInt("clickedMovie");

            Uri builtUri = Uri.parse("http://image.tmdb.org/t/p/").buildUpon()
                    .appendPath("w185")
                    .appendEncodedPath(mMovies.get(id).getPosterPath())
                    .build();

            String url = null;
            url = builtUri.toString();

            Picasso.get().load(url).into(mPosterImageView);

            mTitelTextView.setText(mMovies.get(id).getTitle());
            mYearTextVies.setText(mMovies.get(id).getReleaseDate().substring(0, 4));
            mRatingRatingBar.setRating(mMovies.get(id).getVoteAverage() / 2f);
            mVotesTextView.setText("(" + String.valueOf(mMovies.get(id).getVoteCount()) + ")");
            mGenreTextView.setText(String.valueOf(mMovies.get(id).getGenreIds()[0]));
            mDescriptionTextView.setText(mMovies.get(id).getDescription());

            Toast.makeText(this, String.valueOf(mMovies.get(id).getVoteAverage()), Toast.LENGTH_SHORT).show();
        }

    }
}
