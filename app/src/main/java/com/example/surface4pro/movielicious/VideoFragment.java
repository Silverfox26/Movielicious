package com.example.surface4pro.movielicious;


import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

import com.example.surface4pro.movielicious.utilities.NetworkUtils;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment implements VideoAdapter.VideoAdapterOnClickHandler {
    private RecyclerView mVideosRecyclerView;
    public VideoAdapter mAdapter;
    private TextView mErrorMessage;
    private TextView mNoVideosAvailable;
    private ProgressBar mLoadingIndicator;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
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
        mVideosRecyclerView = view.findViewById(R.id.rv_video_fragment);
        mErrorMessage = view.findViewById(R.id.tv_error_message_video);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator_video);
        mNoVideosAvailable = view.findViewById(R.id.tv_no_videos_message);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mVideosRecyclerView.setLayoutManager(layoutManager);
        mVideosRecyclerView.setHasFixedSize(false);
        mAdapter = new VideoAdapter(this);
        mVideosRecyclerView.setAdapter(mAdapter);

        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedDetailViewModel.class);

        sharedViewModel.getLoadingStatusVideo().observe(this, (Integer loadingStatus) -> {
            if (loadingStatus != null) {
                if (loadingStatus == 1) {
                    // show loading indicator
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                } else if (loadingStatus == 2) {
                    // show recycler view
                    showVideoData();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                } else if (loadingStatus == -1) {
                    showErrorMessage();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                } else if (loadingStatus == 0) {
                    showNoVideosMessage();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
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
        mVideosRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mNoVideosAvailable.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showVideoData() {
        mVideosRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mNoVideosAvailable.setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the RecyclerView and hides the error message
     */
    private void showNoVideosMessage() {
        mVideosRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mNoVideosAvailable.setVisibility(View.VISIBLE);
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
