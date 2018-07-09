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
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    // Create a data binding instance called mBinding of type ActivityMainBinding
    FragmentReviewBinding mBinding;

    private ReviewAdapter mAdapter;

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set the Content View using DataBindingUtil to the activity_main layout
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mBinding.rvReviewFragment.setLayoutManager(layoutManager);
        mBinding.rvReviewFragment.setHasFixedSize(false);
        mAdapter = new ReviewAdapter();
        mBinding.rvReviewFragment.setAdapter(mAdapter);


        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedDetailViewModel.class);

        sharedViewModel.getLoadingStatusReviews().observe(this, (Integer loadingStatus) -> {
            if (loadingStatus != null) {
                if (loadingStatus == 1) {
                    // show loading indicator
                    mBinding.pbLoadingIndicatorReviews.setVisibility(View.VISIBLE);
                } else if (loadingStatus == 2) {
                    // show recycler view
                    showReviewData();
                    mBinding.pbLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
                } else if (loadingStatus == -1) {
                    showErrorMessage();
                    mBinding.pbLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
                } else if (loadingStatus == 0) {
                    showNoReviewMessage();
                    mBinding.pbLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
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
        mBinding.rvReviewFragment.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessageReview.setVisibility(View.VISIBLE);
        mBinding.tvNoReviewsMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showReviewData() {
        mBinding.rvReviewFragment.setVisibility(View.VISIBLE);
        mBinding.tvErrorMessageReview.setVisibility(View.INVISIBLE);
        mBinding.tvNoReviewsMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showNoReviewMessage() {
        mBinding.rvReviewFragment.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessageReview.setVisibility(View.INVISIBLE);
        mBinding.tvNoReviewsMessage.setVisibility(View.VISIBLE);
    }
}
