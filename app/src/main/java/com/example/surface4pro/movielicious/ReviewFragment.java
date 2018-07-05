package com.example.surface4pro.movielicious;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.surface4pro.movielicious.model.Review;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {


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

        TextView reviewTextView = view.findViewById(R.id.tv_review_fragment);

        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedDetailViewModel.class);
        sharedViewModel.getSavedReviewList().observe(this, reviewList -> {
            StringBuilder reviewString = new StringBuilder();
            for (Review review : reviewList) {
                reviewString.append(review.getAuthor());
                reviewString.append("\n");
                reviewString.append(review.getContent());
            }
            reviewTextView.setText(reviewString.toString());
        });
    }
}
