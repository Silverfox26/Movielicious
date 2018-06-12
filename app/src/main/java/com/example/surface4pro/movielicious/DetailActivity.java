package com.example.surface4pro.movielicious;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
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

        // set an exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }

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


            StringBuilder genres = new StringBuilder();
            int[] genreArray = mMovies.get(id).getGenreIds();

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

            mDescriptionTextView.setText(mMovies.get(id).getDescription());

            android.support.v7.app.ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setTitle(mMovies.get(id).getTitle());
            }

            Toast.makeText(this, String.valueOf(mMovies.get(id).getVoteAverage()), Toast.LENGTH_SHORT).show();

        }

    }

}
