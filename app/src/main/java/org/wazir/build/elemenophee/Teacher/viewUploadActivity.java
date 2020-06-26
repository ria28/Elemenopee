package org.wazir.build.elemenophee.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.otherAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.videoRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;

import java.util.ArrayList;
import java.util.Map;


public class viewUploadActivity extends AppCompatActivity implements videoRecyclerAdapter.onLayoutClick {

    RecyclerView recyclerView;
    videoRecyclerAdapter videoAdapter;
    notesRecyclerAdapter notesAdapter;
    otherAdapter otherAdapter;
    CollectionReference reference;

    Spinner viewClass, viewSubject, viewChapter, viewContentType;
    ArrayList<String> Class = new ArrayList<>();
    ArrayList<String> Subject = new ArrayList<>();
    ArrayList<String> Chapter = new ArrayList<>();
    ArrayList<String> Content = new ArrayList<>();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<contentModel> videoList = new ArrayList<>();
    ArrayList<contentModel> pdfList = new ArrayList<>();
    ArrayList<contentModel> otherList = new ArrayList<>();


    ArrayAdapter<String> chapterSpinnerViewAdapter;
    ArrayAdapter<String> subjectSpinnerViewAdapter;
    ArrayAdapter<String> classSpinnerViewAdapter;
    ArrayAdapter<String> contentSpinnerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_upload);

        recyclerView = findViewById(R.id.viewUploadRecycler);
        viewClass = findViewById(R.id.viewUploadClassSpinner);
        viewSubject = findViewById(R.id.viewUploadSubjectSpinner);
        viewChapter = findViewById(R.id.viewUploadChapterSpinner);
        viewContentType = findViewById(R.id.viewUploadContentTypeSpinner);

        Intent intent = getIntent();
        Class = intent.getStringArrayListExtra("CLASSES");
        Subject = intent.getStringArrayListExtra("SUBS");
        Chapter.add("SELECT");
        Content.add("VIDEOS");
        Content.add("NOTES");
        Content.add("OTHER");

        classSpinnerViewAdapter = new ArrayAdapter<String>(viewUploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, Class);
        subjectSpinnerViewAdapter = new ArrayAdapter<String>(viewUploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, Subject);
        chapterSpinnerViewAdapter = new ArrayAdapter<String>(viewUploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, Chapter);
        contentSpinnerViewAdapter = new ArrayAdapter<String>(viewUploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, Content);

        if (Class != null && Subject != null) {
            viewClass.setAdapter(classSpinnerViewAdapter);
            viewSubject.setAdapter(subjectSpinnerViewAdapter);
            viewChapter.setAdapter(chapterSpinnerViewAdapter);
            viewContentType.setAdapter(contentSpinnerViewAdapter);
        }


        videoAdapter = new videoRecyclerAdapter(viewUploadActivity.this, false, videoList, this, -1);
        notesAdapter = new notesRecyclerAdapter(viewUploadActivity.this, pdfList);
        otherAdapter = new otherAdapter(viewUploadActivity.this, otherList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();


        viewClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadChapterSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadChapterSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadChapterContent(viewContentType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewContentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadChapterContent(viewContentType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setUpRecyclerView();


    }

    private void loadChapterContent(final String contentType) {
        if (viewChapter.getSelectedItem().toString() != "SELECT" && viewChapter.getSelectedItem().toString() != "No Chapters") {

            reference = FirebaseFirestore.getInstance().collection("/CLASSES/" +
                    viewClass.getSelectedItem().toString() +
                    "/SUBJECT/" +
                    viewSubject.getSelectedItem().toString() +
                    "/CONTENT"
            );
            reference
                    .whereEqualTo("TEACHER_ID", user.getPhoneNumber())
                    .whereEqualTo("CHAPTER", viewChapter.getSelectedItem().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                videoList.clear();
                                pdfList.clear();
                                otherList.clear();
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
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        if (doc.get("OTHER") != null) {
                                            for (Map<String, Object> obj : (ArrayList<Map<String, Object>>) doc.getData().get("OTHER")) {
                                                otherList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                        , (Timestamp) obj.get("timeStamp"),obj.get("privacy")+"",obj.get("teacherID")+"",obj.get("mime")+""));
                                            }
                                            otherAdapter.notifyDataSetChanged();
                                        }
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "No Data found Related to this Chapter", Toast.LENGTH_SHORT).show();
                                    videoAdapter.notifyDataSetChanged();
                                    notesAdapter.notifyDataSetChanged();
                                    otherAdapter.notifyDataSetChanged();
                                }

                                if (contentType.equalsIgnoreCase("VIDEOS"))
                                    recyclerView.setAdapter(videoAdapter);
                                else if(contentType.equalsIgnoreCase("NOTES"))
                                    recyclerView.setAdapter(notesAdapter);
                                else
                                    recyclerView.setAdapter(otherAdapter);
                            } else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            pdfList.clear();
            videoList.clear();
            otherList.clear();
            videoAdapter.notifyDataSetChanged();
            notesAdapter.notifyDataSetChanged();
            otherAdapter.notifyDataSetChanged();
        }

    }

    private void loadChapterSpinner() {

        reference = FirebaseFirestore.getInstance().collection("/CLASSES/" +
                viewClass.getSelectedItem().toString() +
                "/SUBJECT/" +
                viewSubject.getSelectedItem().toString() +
                "/CONTENT"
        );

        reference
                .whereEqualTo("TEACHER_ID", user.getPhoneNumber())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Chapter.clear();
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Chapter.add(doc.get("CHAPTER").toString());
                                    chapterSpinnerViewAdapter.notifyDataSetChanged();
                                }
                                loadChapterContent(viewContentType.getSelectedItem().toString());
                            } else {
                                Chapter.add("No Chapters");
                                chapterSpinnerViewAdapter.notifyDataSetChanged();
                            }
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpRecyclerView() {
        if (viewContentType.getSelectedItem().toString().equalsIgnoreCase("VIDEOS"))
            recyclerView.setAdapter(videoAdapter);
        else
            recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public void onClicked(int i, boolean isVideoPlaying) {
        Intent intent = new Intent(viewUploadActivity.this, videoPlayingActivity.class);
        intent.putExtra("VIDEO_LINK", videoList.get(i).getFileUrl());
        intent.putExtra("VIDEO_LIST", videoList);
        intent.putExtra("OTHER_LIST", otherList);
        intent.putExtra("PDF_LIST", pdfList);
        intent.putExtra("IS_TEACHER", true);
        intent.putExtra("FROM_RECENT",false);
        startActivity(intent);
    }
}


