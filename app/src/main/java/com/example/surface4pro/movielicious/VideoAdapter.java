package com.example.surface4pro.movielicious;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.surface4pro.movielicious.model.Video;
import com.example.surface4pro.movielicious.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    // Cached copy of Videos
    private List<Video> mVideoData;

    // On-click handler to make it easy for an Activity to interface with the RecyclerView
    private final VideoAdapterOnClickHandler mClickHandler;

    /**
     * Creates a ReviewAdapter.
     */
    public VideoAdapter(VideoAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public interface VideoAdapterOnClickHandler {
        void onClick(View v, String clickedVideoKey, int layoutPosition);
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new VideoAdapter.VideoViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoViewHolder holder, int position) {
        String videoKey = mVideoData.get(position).getKey();
        String videoName = mVideoData.get(position).getName();
        String videoType = mVideoData.get(position).getType();

        holder.mVideoType.setText(videoType);
        holder.mVideoName.setText(videoName);

        // Create the image URL and display it using Picasso
        String url = NetworkUtils.buildVideoImageUrl(videoKey);
        Picasso.get().load(url).placeholder(R.drawable.no_video).into(holder.mVideoImage);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (mVideoData == null) return 0;
        return mVideoData.size();
    }

    /**
     * Method is used to set the review data on a ReviewAdapter if one is already created.
     * This way new data can be loaded from the web and displayed without the need for
     * a new MovieAdapter.
     *
     * @param reviewData The new movie data to be displayed.
     */
    public void setVideoData(List<Video> videoData) {
        mVideoData = videoData;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a video list item.
     */
    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mVideoImage;
        final TextView mVideoName;
        final TextView mVideoType;

        VideoViewHolder(View itemView) {
            super(itemView);
            mVideoImage = itemView.findViewById(R.id.iv_video_image);
            mVideoName = itemView.findViewById(R.id.tv_video_name);
            mVideoType = itemView.findViewById(R.id.tv_video_type);

            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String videoKey = mVideoData.get(adapterPosition).getKey();
            Log.d("AAA_ADAPTER", "onClick: " + videoKey);
            mClickHandler.onClick(v, videoKey, getAdapterPosition());

        }
    }
}
