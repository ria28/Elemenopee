package org.wazir.build.elemenophee.IntLogSigScreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.wazir.build.elemenophee.MainActivity;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    CardView getStartedBtn, NextBtn, SkipBtn;
    IntroAda adapter;
    ViewPager pager;
    TabLayout layout;
    Animation animation;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 5/22/2020 navigate from here to login Direct
        if (restorePrefData()) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_onboarding);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getStartedBtn = findViewById(R.id.btn_id_start);
        layout = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.viewPager);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        final List<IntroMoObj> arr = new ArrayList<>();
        arr.add(new IntroMoObj(R.drawable.img_intro_1, "Community", "Add your doubts and queries to the biggest community of teachers and students"));
        arr.add(new IntroMoObj(R.drawable.img_intro_5, "Interact", "Interact with the teachers, via live and doubt solving sessions, and get to know them better"));
        arr.add(new IntroMoObj(R.drawable.img_intro_3, "Choose", "Choose your own teacher. View the ratings and find the teacher who suits you the best."));
        arr.add(new IntroMoObj(R.drawable.img_intro_4, "Its Always Free", "View as many videos as you want and check as many teachers as you like. It is always free"));
        arr.add(new IntroMoObj(R.drawable.img_intro_6, "Download", "View as many videos as you want and check as many teachers as you like. It is always free"));
        arr.add(new IntroMoObj(R.drawable.img_intro_2, "Enroll For a Month", "View as many videos as you want and check as many teachers as you like. It is always free"));

        adapter = new IntroAda(this, arr);
        pager.setAdapter(adapter);
        layout.setupWithViewPager(pager);

        layout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == arr.size() - 1) {
                    loaddLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getBaseContext(), MainActivity.class);
                startActivity(mainActivity);
                savePrefsData();
                finish();
                Toast.makeText(OnboardingActivity.this, "Get Started Tapped", Toast.LENGTH_SHORT).show();
            }
        });
        NextBtn = findViewById(R.id.btn_id_next);
        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = pager.getCurrentItem();
                if (position < arr.size()) {
                    position++;
                    pager.setCurrentItem(position);
                }
                if (position == arr.size() - 1) {
                    loaddLastScreen();
                }
            }
        });
        SkipBtn = findViewById(R.id.btn_id_skip);
        SkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(arr.size());
            }
        });
    }


    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend", false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.apply();
    }

    private void loaddLastScreen() {
        getStartedBtn.setVisibility(View.VISIBLE);
        NextBtn.setVisibility(View.INVISIBLE);
        SkipBtn.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.INVISIBLE);
        getStartedBtn.setAnimation(animation);
    }
}
