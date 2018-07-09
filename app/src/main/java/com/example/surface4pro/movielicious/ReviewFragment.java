package com.example.surface4pro.movielicious;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {
    private RecyclerView mReviewsRecyclerView;
    private ReviewAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private TextView mNoReviewsAvailableMessage;

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing the View variables
        mErrorMessage = view.findViewById(R.id.tv_error_message_review);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator_reviews);
        mNoReviewsAvailableMessage = view.findViewById(R.id.tv_no_reviews_message);


        mReviewsRecyclerView = view.findViewById(R.id.rv_review_fragment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setHasFixedSize(false);
        mAdapter = new ReviewAdapter();
        mReviewsRecyclerView.setAdapter(mAdapter);


        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedDetailViewModel.class);

        sharedViewModel.getLoadingStatusReviews().observe(this, (Integer loadingStatus) -> {
            if (loadingStatus != null) {
                if (loadingStatus == 1) {
                    // show loading indicator
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                } else if (loadingStatus == 2) {
                    // show recycler view
                    showReviewData();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                } else if (loadingStatus == -1) {
                    showErrorMessage();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                } else if (loadingStatus == 0) {
                    showNoReviewMessage();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                }
            }
        });

        sharedViewModel.getSavedReviewList().observe(this, reviewList -> {

            mAdapter.setReviewData(reviewList);

        });
    }

    /**
     * This method sets the RecyclerView invisible and shows the error message
     */
    private void showErrorMessage() {
        mReviewsRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mNoReviewsAvailableMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showReviewData() {
        mReviewsRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mNoReviewsAvailableMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showNoReviewMessage() {
        mReviewsRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mNoReviewsAvailableMessage.setVisibility(View.VISIBLE);
    }
}
