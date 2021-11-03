package com.example.slideshowdummy;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.slideshowdummy.databinding.VideoPlayerFragmentBinding;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoPlayerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String videoUrl;
    private VideoPlayerFragmentBinding binding;
    private Activity activity;

    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private PlaybackStateListener playbackStateListener;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url Parameter 1.
     * @return A new instance of fragment VideoPlayerFragment.
     */
    public static VideoPlayerFragment newInstance(String url) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoUrl = getArguments().getString(ARG_PARAM1);
        }
        activity = requireActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_player, container, false);
        return binding.getRoot();
    }

    /**
     * This method is only used by viewpager because the viewpager doesn't call onPause after
     * changing the fragment
     */
    public void losingVisibility() {
        // IMPLEMENT YOUR PAUSE CODE HERE
        releasePlayer();
    }

    /**
     * This method is only used by viewpager because the viewpager doesn't call onPause after
     * changing the fragment
     */
    public void gainVisibility() {
        // IMPLEMENT YOUR RESUME CODE HERE
        initializePlayer();
    }

    /**
     * This method is for playing single video
     */
    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(activity).build();
        playbackStateListener = new PlaybackStateListener();
        binding.videoView.setPlayer(player);

        binding.videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.addListener(playbackStateListener);
        player.setRepeatMode(Player.REPEAT_MODE_ONE); // this is for looping only this video

        player.setMediaSource(buildMediaSource(Uri.parse(videoUrl),activity), false);
        player.prepare();
    }

    private MediaSource buildMediaSource(Uri uri, Context context) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context, "exoplayer-dummy-slide-show");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(playbackStateListener);
            player.release();
            player = null;
        }
    }

    private class PlaybackStateListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            boolean isPlaybackReady = false;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    isPlaybackReady = false;
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    isPlaybackReady = false;
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    isPlaybackReady = true;
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    isPlaybackReady = false;
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    isPlaybackReady = false;
                    break;
            }
            Timber.d("changed state to %s, playWhenReady: %s",stateString, playWhenReady);
            if(isPlaybackReady) {
                Timber.d("Duration: %s", player.getDuration());
            }
        }
    }
}