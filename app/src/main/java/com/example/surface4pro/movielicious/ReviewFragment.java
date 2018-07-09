/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surface4pro.movielicious.databinding.FragmentReviewBinding;

import java.util.Objects;

/**
 * Fragment to display the movie's reviews in the DetailActivities's ViewPager.
 */
public class ReviewFragment extends Fragment {

    // Member variable declarations
    private FragmentReviewBinding mBinding;
    private ReviewAdapter mAdapter;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the Content View using DataBindingUtil
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false);

        return mBinding.getRoot();
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

        // Configuring the RecyclerView and setting its adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mBinding.rvReviewFragment.setLayoutManager(layoutManager);
        mBinding.rvReviewFragment.setHasFixedSize(false);
        mAdapter = new ReviewAdapter();
        mBinding.rvReviewFragment.setAdapter(mAdapter);

        // Get the instance of the SharedViewModel
        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedDetailViewModel.class);

        sharedViewModel.getLoadingStatusReviews().observe(this, (Integer loadingStatus) -> {

            // Check the review loading status and update the UI accordingly.
            if (loadingStatus != null) {
                switch (loadingStatus) {
                    case DetailActivity.LOADING_STATUS_LOADING:
                        mBinding.pbLoadingIndicatorReviews.setVisibility(View.VISIBLE);
                        break;
                    case DetailActivity.LOADING_STATUS_LOADING_SUCCESSFUL:
                        showReviewData();
                        mBinding.pbLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
                        break;
                    case DetailActivity.LOADING_STATUS_ERROR:
                        showErrorMessage();
                        mBinding.pbLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
                        break;
                    case DetailActivity.LOADING_STATUS_NO_RESULTS_AVAILABLE:
                        showNoReviewMessage();
                        mBinding.pbLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        // Get and display the reviews using an observer.
        sharedViewModel.getSavedReviewList().observe(this, reviewList -> {
            mAdapter.setReviewData(reviewList);
        });
    }

    /**
     * This method shows the error message
     * and hides the RecyclerView and the no reviews message
     */
    private void showErrorMessage() {
        mBinding.rvReviewFragment.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessageReview.setVisibility(View.VISIBLE);
        mBinding.tvNoReviewsMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView
     * and hides the error message and the no reviews message.
     */
    private void showReviewData() {
        mBinding.rvReviewFragment.setVisibility(View.VISIBLE);
        mBinding.tvErrorMessageReview.setVisibility(View.INVISIBLE);
        mBinding.tvNoReviewsMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the no reviews message
     * and hides the error message and the RecyclerView.
     */
    private void showNoReviewMessage() {
        mBinding.rvReviewFragment.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessageReview.setVisibility(View.INVISIBLE);
        mBinding.tvNoReviewsMessage.setVisibility(View.VISIBLE);
    }
}
