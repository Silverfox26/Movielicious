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

import com.example.surface4pro.movielicious.model.Video;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        TextView videoTextView = view.findViewById(R.id.tv_videos_fragment);

        SharedDetailViewModel sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedDetailViewModel.class);
        sharedViewModel.getSavedVideoList().observe(this, videoList -> {
            StringBuilder videoString = new StringBuilder();
            for (Video video : videoList) {
                videoString.append(video.getKey());
                videoString.append("\n");
                videoString.append(video.getName());
                videoString.append("\n");
                videoString.append(video.getSite());
                videoString.append("\n");
                videoString.append(video.getType());
                videoString.append("\n\n");
            }
            videoTextView.setText(videoString.toString());
        });
    }
}
