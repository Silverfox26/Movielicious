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


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {
    private RecyclerView mReviewsRecyclerView;
    private ReviewAdapter mAdapter;

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
        mReviewsRecyclerView = view.findViewById(R.id.rv_review_fragment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setHasFixedSize(false);
        mAdapter = new ReviewAdapter();
        mReviewsRecyclerView.setAdapter(mAdapter);

        // TextView reviewTextView = view.findViewById(R.id.tv_review_fragment);

        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedDetailViewModel.class);
        sharedViewModel.getSavedReviewList().observe(this, reviewList -> {

            mAdapter.setReviewData(reviewList);
//            StringBuilder reviewString = new StringBuilder();
//            for (Review review : reviewList) {
//                reviewString.append(review.getAuthor());
//                reviewString.append("\n");
//                reviewString.append(review.getContent());
//                reviewString.append("\n\n");
//            }
//            reviewTextView.setText(reviewString.toString());
        });
    }
}
