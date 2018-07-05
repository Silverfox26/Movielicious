package com.example.surface4pro.movielicious;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DetailViewFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public DetailViewFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DescriptionFragment();
            case 1:
                return new VideoFragment();
            case 2:
                return new ReviewFragment();
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_description);
            case 1:
                return mContext.getString(R.string.category_videos);
            case 2:
                return mContext.getString(R.string.category_reviews);
            default:
                return null;
        }
    }
}
