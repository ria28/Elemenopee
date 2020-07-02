package org.wazir.build.elemenophee;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.Student.StudentSupport.Chat121.MessageActivity;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.otherAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.videoRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;

import java.util.ArrayList;

public class ViewTeacherProfile extends AppCompatActivity implements videoRecyclerAdapter.onLayoutClick {

    ImageView profilePic;
    TextView teacherName, schoolName, connections, subscribe, videoCount;
    RecyclerView recyclerView;
    videoRecyclerAdapter videoAdapter;
    otherAdapter otherAdapter;
    notesRecyclerAdapter notesAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String teacher_ID;
    boolean isSubscriber = false;
    CollectionReference Sref;
    TeacherObj teacherObj;
    LoadingPopup loadingPopup;
    ImageView message;
    TextView bio;

    Spinner FileType;

    ArrayList<contentModel> videoList = new ArrayList<>();
    ArrayList<contentModel> pdfList = new ArrayList<>();
    ArrayList<contentModel> otherList = new ArrayList<>();

    CollectionReference reference;
    CollectionReference teacherReference;
    CollectionReference SubsRef;


    ArrayAdapter<String> FileTypeSpinnerViewAdapter;
    ArrayList<String> content = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        message = findViewById(R.id.message_imageView);
        bio = findViewById(R.id.textView58);
        teacher_ID = getIntent().getStringExtra("TEACHER_ID");
        reference = FirebaseFirestore.getInstance().collection(
                "/TEACHERS/" +
                        teacher_ID +
                        "/RECENT_UPLOADS"
        );
        teacherReference = FirebaseFirestore.getInstance().collection("/TEACHERS/");
        SubsRef = FirebaseFirestore.getInstance().collection(
                "/TEACHERS/" +
                        teacher_ID +
                        "/SUBSCRIBERS"
        );

        loadingPopup = new LoadingPopup(ViewTeacherProfile.this);
        loadingPopup.dialogRaise();
        init();
        getTeacherData();

        content.add("VIDEOS");
        content.add("NOTES");
        content.add("OTHER");

        FileTypeSpinnerViewAdapter = new ArrayAdapter<>(ViewTeacherProfile.this,
                android.R.layout.simple_spinner_dropdown_item, content);

        FileType.setAdapter(FileTypeSpinnerViewAdapter);


        videoAdapter = new videoRecyclerAdapter(ViewTeacherProfile.this, false, videoList, this, -1,false);
        notesAdapter = new notesRecyclerAdapter(ViewTeacherProfile.this, pdfList,false);
        otherAdapter = new otherAdapter(ViewTeacherProfile.this, otherList,false);

        loadData("VIDEOS");
        loadData("NOTES");
        loadData("OTHER");


        FileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUpRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        setUpRecyclerView();


        subscribe.setOnClickListener(v -> {
            if (subscribe.getText().toString().equalsIgnoreCase("SUBSCRIBE")) {
                Intent intent = new Intent(ViewTeacherProfile.this, PaymentActivity.class);
                if (!teacher_ID.isEmpty()) {
                    intent.putExtra("TEACHER_ID", teacher_ID);
                    startActivity(intent);
                }
            }
        });


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTeacherProfile.this, MessageActivity.class);
                if (!teacher_ID.isEmpty()) {
                    intent.putExtra("user_id", teacher_ID);
                    startActivity(intent);
                }
            }
        });


    }

    private void getTeacherData() {

        teacherReference
                .document(teacher_ID)
                .collection("SUBSCRIBERS")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        connections.setText(String.valueOf(task.getResult().size()));
                    } else {
                        Toast.makeText(ViewTeacherProfile.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                    }
                });

        teacherReference.document(teacher_ID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    teacherObj = documentSnapshot.toObject(TeacherObj.class);
                    videoCount.setText(String.valueOf(teacherObj.getVideoCount()));
                    teacherName.setText(teacherObj.getName());
                    schoolName.setText(teacherObj.getSchool());
                    bio.setText(teacherObj.getBio());
                    Glide.with(ViewTeacherProfile.this).load(teacherObj.getProPicURL()).into(profilePic);
                    SubsRef.whereEqualTo("studentID" +
                            "", user.getPhoneNumber())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.size() == 1) {
                            subscribe.setText("SUBSCRIBED");
                            subscribe.setTextColor(Color.parseColor("#eac7c7"));
                            loadingPopup.dialogDismiss();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(ViewTeacherProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingPopup.dialogDismiss();
                    });
                }).addOnFailureListener(e -> {
            Toast.makeText(ViewTeacherProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            loadingPopup.dialogDismiss();
            onBackPressed();
        });
    }

    private void setUpRecyclerView() {
        if (FileType.getSelectedItem().toString() == "VIDEOS")
            recyclerView.setAdapter(videoAdapter);
        else if (FileType.getSelectedItem().toString() == "NOTES")
            recyclerView.setAdapter(notesAdapter);
        else if (FileType.getSelectedItem().toString() == "OTHER") {
            recyclerView.setAdapter(otherAdapter);
        }
    }


    private void loadData(final String type) {
        reference.document("UPLOADS")
                .collection(type)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (type == "VIDEOS") {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    contentModel temp = doc.toObject(contentModel.class);
                    if (temp.getPrivacy().equalsIgnoreCase("private")) {
                        if (isSubscriber)
                            videoList.add(temp);
                    } else
                        videoList.add(temp);
                    videoAdapter.notifyDataSetChanged();
                }
            } else if (type == "NOTES") {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    contentModel temp = doc.toObject(contentModel.class);
                    if (temp.getPrivacy().equalsIgnoreCase("private")) {
                        if (isSubscriber)
                            pdfList.add(temp);
                    } else
                        pdfList.add(temp);
                    notesAdapter.notifyDataSetChanged();
                }

            } else if (type == "OTHER") {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    contentModel temp = doc.toObject(contentModel.class);
                    if (temp.getPrivacy().equalsIgnoreCase("private")) {
                        if (isSubscriber)
                            otherList.add(temp);
                    } else
                        otherList.add(temp);
                    notesAdapter.notifyDataSetChanged();
                }
            }
            loadingPopup.dialogDismiss();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            loadingPopup.dialogDismiss();
        });
    }

    private void init() {
        profilePic = findViewById(R.id.viewTeacherProfilePicture);
        teacherName = findViewById(R.id.viewTeacherProfileName);
        schoolName = findViewById(R.id.viewTeacherProfileSchoolName);
        connections = findViewById(R.id.viewTeacherProfileConnections);
        subscribe = findViewById(R.id.viewTeacherProfileSubscribe);
        recyclerView = findViewById(R.id.viewTeacherProfile_VideoRecycler);
        FileType = findViewById(R.id.TeacherProfileRecentSpinner);
        videoCount = findViewById(R.id.id_view_video_count);
        Sref = FirebaseFirestore.getInstance().collection(
                "/TEACHERS/" +
                        teacher_ID +
                        "/SUBSCRIBERS"
        );
        Sref.whereEqualTo("studentID", user.getPhoneNumber())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 1) {
                        isSubscriber = true;
                    }
                }).addOnFailureListener(e -> Toast.makeText(ViewTeacherProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onClicked(int i, boolean isVideoPlaying) {
        Intent intent = new Intent(ViewTeacherProfile.this, videoPlayingActivity.class);
        intent.putExtra("VIDEO_LINK", videoList.get(i).getFileUrl());
        intent.putExtra("VIDEO_LIST", videoList);
        intent.putExtra("PDF_LIST", pdfList);
        intent.putExtra("OTHER_LIST", otherList);
        intent.putExtra("FROM_RECENT", true);
        startActivity(intent);
    }

}