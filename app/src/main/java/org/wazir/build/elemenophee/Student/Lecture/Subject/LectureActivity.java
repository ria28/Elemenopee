package org.wazir.build.elemenophee.Student.Lecture.Subject;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import org.wazir.build.elemenophee.R;


public class LectureActivity extends AppCompatActivity {

//    FragMainStu main;
//    View view;
////    final int start=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

//        main = new FragMainStu();
//
//        FragmentManager manager1 = getSupportFragmentManager();
//        manager1.beginTransaction()
//                 .replace(R.id.frame_container_stu, main)
//                 .commit();

    }

}
