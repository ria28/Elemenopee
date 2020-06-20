package org.wazir.build.elemenophee.Student.Lecture.Subject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.adapter.otherAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.videoRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;

import java.util.ArrayList;
import java.util.Map;

public class ViewVideoListActivity extends AppCompatActivity implements videoRecyclerAdapter.onLayoutClick{

    ArrayList<contentModel> videoList = new ArrayList<>();
    ArrayList<contentModel> pdfList = new ArrayList<>();

    videoRecyclerAdapter videoAdapter;
    otherAdapter notesAdapter;
    RecyclerView recyclerView;
    CollectionReference reference;
    String chapterTitle;
    String SubName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video_list);
        recyclerView= findViewById(R.id.viewVideoListRecycler);

        Intent intent = getIntent();
        chapterTitle = intent.getStringExtra("CHAPTER_TITLE");
        SubName = intent.getStringExtra("SUBJECT_NAME");
        videoAdapter = new videoRecyclerAdapter(ViewVideoListActivity.this, false, videoList, this, -1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        loadVideoList();
        recyclerView.setAdapter(videoAdapter);

    }

    private void loadVideoList() {

        reference = FirebaseFirestore.getInstance().collection("CLASSES")
                .document("Class 6").collection("SUBJECT").document(SubName)
                .collection("CONTENT");

        reference.whereEqualTo("CHAPTER", chapterTitle)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            videoList.clear();
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.get("VIDEOS") != null) {
                                        for (Map<String, Object> obj : (ArrayList<Map<String, Object>>) doc.getData().get("VIDEOS")) {
                                            videoList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                    , (Timestamp) obj.get("timeStamp"),obj.get("privacy")+"",obj.get("teacherID")+"",obj.get("mime")+""));
                                        }
                                        videoAdapter.notifyDataSetChanged();
                                    }
                                }

                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.get("NOTES") != null) {
                                        for (Map<String, Object> obj : (ArrayList<Map<String, Object>>) doc.getData().get("NOTES")) {
                                            pdfList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                    , (Timestamp) obj.get("timeStamp"),obj.get("privacy")+"",obj.get("teacherID")+"",obj.get("mime")+""));
                                        }
                                        notesAdapter.notifyDataSetChanged();
                                    }
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "No Data found Related to this Chapter", Toast.LENGTH_SHORT).show();
                                videoAdapter.notifyDataSetChanged();
                                notesAdapter.notifyDataSetChanged();
                            }
                            recyclerView.setAdapter(videoAdapter);
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
    }

    @Override
    public void onClicked(int i, boolean isVideoPlaying) {
        Intent intent = new Intent(ViewVideoListActivity.this, videoPlayingActivity.class);
        intent.putExtra("VIDEO_LINK", videoList.get(i).getFileUrl());
        intent.putExtra("VIDEO_LIST", videoList);
        intent.putExtra("PDF_LIST", pdfList);
        startActivity(intent);
    }
}