package com.example.surface4pro.movielicious;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.surface4pro.movielicious.model.Movie;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;
    private ArrayList<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundleObject = getIntent().getExtras();

        if (bundleObject != null && bundleObject.containsKey("movies")) {
            mMovies = bundleObject.getParcelableArrayList("movies");

            Toast.makeText(this, mMovies.get(1).getOriginalTitle(), Toast.LENGTH_SHORT).show();
        }

    }
}
