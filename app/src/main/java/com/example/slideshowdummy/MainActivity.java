package com.example.slideshowdummy;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.example.slideshowdummy.databinding.MainActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private final List<ItemType> itemTypes = new ArrayList<>();
    private int mCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initData();
        setUpViewPager();
    }

    /**
     * Taken From:
     * Image: https://unsplash.com/s/collections/test
     * Video: https://gist.github.com/jsturgis/3b19447b304616f18657
     */
    private void initData() {
        itemTypes.add(new ItemType(ItemType.Type.IMAGE, "https://images.unsplash.com/photo-1596421138661-5fc388411cba?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1887&q=80"));
        itemTypes.add(new ItemType(ItemType.Type.IMAGE, "https://images.unsplash.com/photo-1586877413114-2bc70e05ce27?ixid=MnwxMjA3fDB8MHxjb2xsZWN0aW9uLXBhZ2V8MjB8OTM4NTI4Nnx8ZW58MHx8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        itemTypes.add(new ItemType(ItemType.Type.VIDEO, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
        itemTypes.add(new ItemType(ItemType.Type.IMAGE, "https://images.unsplash.com/photo-1588591451327-d1101da36a5a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxjb2xsZWN0aW9uLXBhZ2V8Mnw5NTEwODE1fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"));
        itemTypes.add(new ItemType(ItemType.Type.VIDEO, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"));
        itemTypes.add(new ItemType(ItemType.Type.VIDEO, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"));
        itemTypes.add(new ItemType(ItemType.Type.IMAGE, "https://images.unsplash.com/photo-1571198317078-76a4b545b2c1?ixid=MnwxMjA3fDB8MHxjb2xsZWN0aW9uLXBhZ2V8N3w5NTEwODE1fHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
    }

    private void setUpViewPager() {
        var adapter = new MediaTypesPagerAdapter(getSupportFragmentManager());
        adapter.setSteps(itemTypes);
        binding.viewpager.setAdapter(adapter);
        //if, item size not big
        binding.viewpager.setOffscreenPageLimit(itemTypes.size());
        //if big set limit to 3 in which case video will start from beginning
//        binding.viewpager.setOffscreenPageLimit(3);

        // adding bottom dots
        addBottomDots(0);

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

                if(adapter.getCachedItem(mCurrentItem) instanceof VideoPlayerFragment) {
                    VideoPlayerFragment cachedFragmentLeaving = (VideoPlayerFragment) adapter.getCachedItem(mCurrentItem);
                    if (cachedFragmentLeaving != null) {
                        cachedFragmentLeaving.losingVisibility();
                    }
                }
                mCurrentItem = position;
                if(adapter.getCachedItem(mCurrentItem) instanceof VideoPlayerFragment) {
                    VideoPlayerFragment cachedFragmentEntering = (VideoPlayerFragment) adapter.getCachedItem(mCurrentItem);
                    if (cachedFragmentEntering != null) {
                        cachedFragmentEntering.gainVisibility();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[itemTypes.size()];

        binding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.half_transparent));
            binding.layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(ContextCompat.getColor(this, R.color.darker_gray));
    }
}