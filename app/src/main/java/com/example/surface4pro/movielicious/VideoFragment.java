package com.example.surface4pro.movielicious;


import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surface4pro.movielicious.databinding.FragmentVideoBinding;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment implements VideoAdapter.VideoAdapterOnClickHandler {

    // Create a data binding instance called mBinding of type ActivityMainBinding
    FragmentVideoBinding mBinding;

    public VideoAdapter mAdapter;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the Content View using DataBindingUtil to the activity_main layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);

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
        mBinding.rvVideoFragment.setLayoutManager(layoutManager);
        mBinding.rvVideoFragment.setHasFixedSize(false);
        mAdapter = new VideoAdapter(this);
        mBinding.rvVideoFragment.setAdapter(mAdapter);

        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedDetailViewModel.class);

        sharedViewModel.getLoadingStatusVideo().observe(this, (Integer loadingStatus) -> {
            if (loadingStatus != null) {
                if (loadingStatus == 1) {
                    // show loading indicator
                    mBinding.pbLoadingIndicatorVideo.setVisibility(View.VISIBLE);
                } else if (loadingStatus == 2) {
                    // show recycler view
                    showVideoData();
                    mBinding.pbLoadingIndicatorVideo.setVisibility(View.INVISIBLE);
                } else if (loadingStatus == -1) {
                    showErrorMessage();
                    mBinding.pbLoadingIndicatorVideo.setVisibility(View.INVISIBLE);
                } else if (loadingStatus == 0) {
                    showNoVideosMessage();
                    mBinding.pbLoadingIndicatorVideo.setVisibility(View.INVISIBLE);
                }
            }
        });

        sharedViewModel.getSavedVideoList().observe(this, videoList -> {
            mAdapter.setVideoData(videoList);
        });
    }

    /**
     * This method sets the RecyclerView invisible and shows the error message
     */
    private void showErrorMessage() {
        mBinding.rvVideoFragment.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessageVideo.setVisibility(View.VISIBLE);
        mBinding.tvNoVideosMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showVideoData() {
        mBinding.rvVideoFragment.setVisibility(View.VISIBLE);
        mBinding.tvErrorMessageVideo.setVisibility(View.INVISIBLE);
        mBinding.tvNoVideosMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showNoVideosMessage() {
        mBinding.rvVideoFragment.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessageVideo.setVisibility(View.INVISIBLE);
        mBinding.tvNoVideosMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v, String videoKey, int layoutPosition) {

        Uri webUri = NetworkUtils.buildYouTubeVideoURI(videoKey);
        Uri appUri = NetworkUtils.buildYouTubeAppVideoURI(videoKey);

        Intent appIntent = new Intent(Intent.ACTION_VIEW, appUri);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
