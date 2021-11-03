package com.example.slideshowdummy;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class MediaTypesPagerAdapter extends FragmentStatePagerAdapter {
    private SparseArray<Fragment> mFragmentsHolded = new SparseArray<>();
    private List<ItemType> mItemTypes;

    MediaTypesPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    void setSteps(List<ItemType> steps) {
        mItemTypes = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        if(fragment instanceof ImageViewerFragment) {
            mFragmentsHolded.append(position, (ImageViewerFragment) fragment);
        }
        if(fragment instanceof VideoPlayerFragment) {
            mFragmentsHolded.append(position, (VideoPlayerFragment) fragment);
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mFragmentsHolded.delete(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getCachedItem(int position) {
        return mFragmentsHolded.get(position, null);
    }

    @Override
    public Fragment getItem(int position) {
        ItemType itemType = mItemTypes.get(position);
        if (itemType.getType() == ItemType.Type.VIDEO)
            return VideoPlayerFragment.newInstance(itemType.getUrl());
        return ImageViewerFragment.newInstance(itemType.getUrl());
    }

    @Override
    public int getCount() {
        return mItemTypes == null ? 0 : mItemTypes.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItemTypes.get(position).getId();
    }
}
