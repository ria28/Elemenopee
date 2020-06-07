package org.wazir.build.elemenophee.Student.Lecture.Subject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class ViewVideosChapActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    String SubName;
    Context context;
    ArrayList<ChapterVideoObject> Chapter = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection("CLASSES").document("Class 6").collection("SUBJECT");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_videos);

        recyclerView = findViewById(R.id.viewVideoChapterRecycler);

        Intent intent = getIntent();
        SubName = intent.getStringExtra("SUBJECT_NAME");

        loadChapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new VideoChapterAdapter(context,Chapter);
        recyclerView.setAdapter(mAdapter);


    }

    private void loadChapter() {

        reference.document("English").collection("CONTENT")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Chapter.clear();
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if(doc.get("VIDEOS")!=null)
                                    Chapter.add(new ChapterVideoObject(doc.get("CHAPTER").toString()));
                                    mAdapter.notifyDataSetChanged();
//                            Log.d("size", "onComplete "+ count);
                                }
                            } else {
                                Chapter.add(new ChapterVideoObject("No Chapters"));
                                mAdapter.notifyDataSetChanged();
                            }
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}