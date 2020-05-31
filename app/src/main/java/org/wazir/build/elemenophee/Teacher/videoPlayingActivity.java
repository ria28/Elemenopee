package org.wazir.build.elemenophee.Teacher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.tabs.TabLayout;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.Fragments.notesFrag;
import org.wazir.build.elemenophee.Teacher.Fragments.videoFrag;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class videoPlayingActivity extends AppCompatActivity {

    PlayerView playerview;
    ProgressBar progressBar;
    ImageView fullScreen;
    SimpleExoPlayer simpleExoPlayer;
    boolean fScreen = false;

    ViewPager viewPager;
    TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playing);

       initVideoPlayer();

       viewPager = findViewById(R.id.playActivityViewPager);
       tabLayout = findViewById(R.id.playActivityTabLayout);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    void initVideoPlayer(){
        playerview = findViewById(R.id.exo_videoPlayer);
        progressBar = findViewById(R.id.exo_progressBar);
        fullScreen =findViewById(R.id.bt_fullscreen);

        //fullScreen Mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Uri videoURL = Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

        //Initiazile LoadController
        LoadControl loadControl = new DefaultLoadControl();

        //initialise bandwidth meter
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        //initialize track selector

        TrackSelector trackSelector = new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );

        //initialise simple exo player

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(videoPlayingActivity.this, trackSelector, loadControl);

        //initialse data source factory

        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exxoplayer_video");

        //initialise extractors factory
        ExtractorsFactory exoPlayerFactory = new DefaultExtractorsFactory();

        //initialise media source
        MediaSource mediaSource = new ExtractorMediaSource(
                videoURL, factory ,exoPlayerFactory ,null ,null
        );

        //set player
        playerview.setPlayer(simpleExoPlayer);

        //keep screen on
        playerview.setKeepScreenOn(true);

        //prepare media

        simpleExoPlayer.prepare(mediaSource);
        //play when ready
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                //check condition
                if(playbackState == Player.STATE_BUFFERING){
                    //when buffering add progressBar
                    progressBar.setVisibility(View.VISIBLE);
                }else if(playbackState == Player.STATE_READY){
                    //when ready hide progressBar
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        fullScreen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                //check Condition
                if(fScreen){
                    //when flag is true
                    //set enter full screen image
                    fullScreen.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_fullscreen)
                    );
                    //set portrait orientation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    playerview.getLayoutParams().height = 0;
                    playerview.getLayoutParams().width = 0;

                    //set fScreen false

                    fScreen = false;
                }else{
                    //when fScreen is false set Full screen exit image
                    fullScreen.setImageDrawable(getDrawable(R.drawable.ic_fullscreen_exit));
                    //set landscape orientation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    playerview.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

                    //set fScreen true

                    fScreen = true;

                }
            }
        });
    }


    private void setUpViewPager(ViewPager Pager){
        playActivity_ViewPagerAdapter adapter = new playActivity_ViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new videoFrag(),"Videos");
        adapter.addFragment(new notesFrag(),"Notes");
        Pager.setAdapter(adapter);
    }




    @Override
    protected void onPause() {
        super.onPause();
        //stop video when ready
        simpleExoPlayer.setPlayWhenReady(false);
        //get the PlayBack state
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //play video when ready
        simpleExoPlayer.setPlayWhenReady(true);
        //get the PlayBack state
        simpleExoPlayer.getPlaybackState();
    }
}
