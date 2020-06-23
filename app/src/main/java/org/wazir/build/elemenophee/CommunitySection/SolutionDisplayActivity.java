package org.wazir.build.elemenophee.CommunitySection;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.wazir.build.elemenophee.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SolutionDisplayActivity extends AppCompatActivity {
    String quesId;

    // layout Elements
    CircleImageView quesUserPic;
    TextView quesUserName, ques;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_display);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getIntentData();
        initLayout();
    }

    void getIntentData() {
        quesId = getIntent().getStringExtra("QUESTION_ID");
    }

    void initLayout() {

    }
}