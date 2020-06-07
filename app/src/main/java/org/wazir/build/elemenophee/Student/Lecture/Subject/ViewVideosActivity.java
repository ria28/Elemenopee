package org.wazir.build.elemenophee.Student.Lecture.Subject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.R;

public class ViewVideosActivity extends AppCompatActivity {

    String SubName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection("CLASSES").document("Class 6").collection("SUBJECT");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_videos);

        Intent intent = getIntent();
        SubName = intent.getStringExtra("SUBJECT_NAME");

//        loadChapter();


    }

//    private void loadChapter() {
//
//        reference.document(SubName).collection("CONTENT")
//                .get()
//    }
}