/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.surface4pro.movielicious.model.Review;

import java.util.List;

/**
 * {@link ReviewAdapter} exposes a List of movie reviews to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    // Cached copy of Reviews
    private List<Review> mReviewData;

    /**
     * Creates a ReviewAdapter.
     */
    public ReviewAdapter() {
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
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        String authorName = mReviewData.get(position).getAuthor();
        String reviewContent = mReviewData.get(position).getContent();

        holder.mAuthorName.setText(authorName);
        holder.mReviewContent.setText(reviewContent);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (mReviewData == null) return 0;
        return mReviewData.size();
    }

    /**
     * Method is used to set the review data on a ReviewAdapter if one is already created.
     * This way new data can be loaded from the web and displayed without the need for
     * a new ReviewAdapter.
     *
     * @param reviewData The new movie data to be displayed.
     */
    public void setReviewData(List<Review> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a review list item.
     */
    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView mAuthorName;
        final TextView mReviewContent;

        ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorName = itemView.findViewById(R.id.tv_review_author);
            mReviewContent = itemView.findViewById(R.id.tv_review_content);
        }
    }
}