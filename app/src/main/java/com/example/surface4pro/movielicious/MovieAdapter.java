/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.surface4pro.movielicious.model.Movie;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link MovieAdapter} exposes an ArrayList of movies to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // Cached copy of Movies
    private List<Movie> mMovieData;

    // On-click handler to make it easy for an Activity to interface with the RecyclerView
    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * Creates a MovieAdapter.
     */
    MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(View v, int clickedMovieId, int layoutPosition);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        String movieImagePath = mMovieData.get(position).getPosterPath();
        String movieTitle = mMovieData.get(position).getTitle();

        holder.mMovieTitle.setText(movieTitle);

        // Create the image URL and display it using Picasso
        String url = NetworkUtils.buildImageUrl(movieImagePath);
        Picasso.get().load(url).placeholder(R.drawable.no_poster).into(holder.mMovieImageView);
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mMovieImageView;
        final TextView mMovieTitle;

        MovieViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.iv_movie_poster);
            mMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int id = mMovieData.get(adapterPosition).getId();
            Log.d("AAA_ADAPTER", "onClick: " + id);
            mClickHandler.onClick(v, id, getAdapterPosition());

        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.size();
    }

    /**
     * Method is used to set the movie data on a MovieAdapter if one is already created.
     * This way new data can be loaded from the web and displayed without the need for
     * a new MovieAdapter.
     *
     * @param movieData The new movie data to be displayed.
     */
    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }


}
