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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surface4pro.movielicious.databinding.FragmentDescriptionBinding;

import java.util.Objects;

/**
 * Fragment to display the movie's description in the DetailActivities's ViewPager.
 */
public class DescriptionFragment extends Fragment {

    // Create data binding instance
    private FragmentDescriptionBinding mBinding;

    public DescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set the Content View using DataBindingUtil
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_description, container, false);

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

        // Get the instance of the SharedViewModel and retrieve the movie's description
        // from the database using an observer.
        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedDetailViewModel.class);
        sharedViewModel.getSavedMovie().observe(this, movie ->
                mBinding.tvDescriptionFragment.setText(Objects.requireNonNull(movie).getDescription()));
    }
}