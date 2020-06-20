package org.wazir.build.elemenophee.Teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.Fragments.notesFrag;
import org.wazir.build.elemenophee.Teacher.Fragments.otherFrag;
import org.wazir.build.elemenophee.Teacher.Fragments.videoFrag;
import org.wazir.build.elemenophee.Teacher.adapter.playActivity_ViewPagerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.ViewTeacherProfile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class videoPlayingActivity extends AppCompatActivity {

    PlayerView playerview;
    ProgressBar progressBar;
    ImageView fullScreen;
    SimpleExoPlayer simpleExoPlayer;
    ConstraintLayout layout;
    boolean isTeacher = false;
    boolean fScreen = false;
    boolean fromRecent;
    MediaSource mediaSource;

    ViewPager viewPager;
    TabLayout tabLayout;

    CircleImageView proPic;
    TextView TeacherName;
    CardView viewProfileCard;
    int playingVideoPosition = -1;

    public String notes_link;
    public ArrayList<contentModel> videoList;
    public ArrayList<contentModel> pdfList;
    public ArrayList<contentModel> otherList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playing);

        setupIntentData();
        init();
        loadTeacherData();
        initVideoPlayer();

        viewPager = findViewById(R.id.playActivityViewPager);
        tabLayout = findViewById(R.id.playActivityTabLayout);
        layout = findViewById(R.id.videoContainingLayout);


        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    void setupIntentData() {
        notes_link = getIntent().getStringExtra("VIDEO_LINK");
        videoList = getIntent().getParcelableArrayListExtra("VIDEO_LIST");
        otherList = getIntent().getParcelableArrayListExtra("OTHER_LIST");
        pdfList = getIntent().getParcelableArrayListExtra("PDF_LIST");
        isTeacher = getIntent().getBooleanExtra("IS_TEACHER", false);
        playingVideoPosition = getIntent().getIntExtra("PLAYING_VIDEO_POSITION", 0);
        fromRecent = getIntent().getBooleanExtra("FROM_RECENT", true);

    }

    private void loadTeacherData() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("/TEACHERS/");
        reference
                .document(videoList.get(playingVideoPosition).getTeacherID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Glide.with(videoPlayingActivity.this)
                                .load(documentSnapshot.get("proPicURL"))
                                .into(proPic);
                        TeacherName.setText(documentSnapshot.get("name") + "");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    void initVideoPlayer() {
        playerview = findViewById(R.id.exo_videoPlayer);
        progressBar = findViewById(R.id.exo_progressBar);
        fullScreen = findViewById(R.id.bt_fullscreen);


        //fullScreen Mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("elemonphee");

        //initialise extractors factory
        ExtractorsFactory exoPlayerFactory = new DefaultExtractorsFactory();

        //initialise media source

        mediaSource = new ExtractorMediaSource(
                Uri.parse(notes_link), factory, exoPlayerFactory, null, null
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
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    playerview.getLayoutParams().height = (int) getResources().getDimension(R.dimen.videoContainerPortraitHeight);
                    playerview.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


                    //set fScreen false

                    fScreen = false;
                } else {
                    //when fScreen is false set Full screen exit image
                    fullScreen.setImageDrawable(getDrawable(R.drawable.ic_fullscreen_exit));
                    //set landscape orientation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    playerview.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    playerview.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);

                    //set fScreen true

                    fScreen = true;

                }
            }
        });
    }


    private void setUpViewPager(ViewPager Pager) {
        playActivity_ViewPagerAdapter adapter = new playActivity_ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new videoFrag(playingVideoPosition, videoList, pdfList, otherList, isTeacher), "Videos");
        adapter.addFragment(new notesFrag(fromRecent, pdfList), "Notes");
        adapter.addFragment(new otherFrag(fromRecent, otherList), "Other");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }

    void init() {
        proPic = findViewById(R.id.teacherPicVideoPlay);
        TeacherName = findViewById(R.id.teacherNameVideoPlay);
        viewProfileCard = findViewById(R.id.cardViewProfileVideoPlay);

        if (isTeacher) {
            viewProfileCard.setVisibility(View.GONE);
        } else {
            viewProfileCard.setVisibility(View.VISIBLE);
        }
    }

    public void navToTeacProfile(View view) {
        String tID = videoList.get(0).getTeacherID();
        Intent intent = new Intent(videoPlayingActivity.this, ViewTeacherProfile.class);
        intent.putExtra("TEACHER_ID", tID);
        startActivity(intent);
    }
}
