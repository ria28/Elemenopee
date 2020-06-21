package org.wazir.build.elemenophee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.otherAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.videoRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewTeacherProfile extends AppCompatActivity implements videoRecyclerAdapter.onLayoutClick {

    CircleImageView profilePic;
    TextView teacherName, schoolName, connections, subscribe;
    RecyclerView recyclerView;
    videoRecyclerAdapter videoAdapter;
    otherAdapter otherAdapter;
    notesRecyclerAdapter notesAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressDialog progress;
    String teacher_ID;
    boolean isSubscriber = false;
    CollectionReference Sref;
    TeacherObj teacherObj;
    LoadingPopup loadingPopup;

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


        progress = new ProgressDialog(this);
        progress.setMessage("Retreiving data..");
        progress.setIndeterminate(true);

        progress.show();

        videoAdapter = new videoRecyclerAdapter(ViewTeacherProfile.this, false, videoList, this, -1);
        notesAdapter = new notesRecyclerAdapter(ViewTeacherProfile.this, pdfList);
        otherAdapter = new otherAdapter(ViewTeacherProfile.this, otherList);

        loadData("VIDEOS");
        loadData("NOTES");
        loadData("OTHER");

        FileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (videoList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "No Recent Video Uploaded", Toast.LENGTH_SHORT).show();
                }
                if (pdfList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "No Recent PDF Uploaded", Toast.LENGTH_SHORT).show();
                }
                if (otherList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "No Recent Note Uploaded", Toast.LENGTH_SHORT).show();
                }
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


        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subscribe.getText().toString().equalsIgnoreCase("SUBSCRIBE")) {
                    Intent intent = new Intent(ViewTeacherProfile.this, PaymentActivity.class);
                    if (!teacher_ID.isEmpty()) {
                        intent.putExtra("TEACHER_ID", teacher_ID);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void getTeacherData() {
        teacherReference.document(teacher_ID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        teacherObj = documentSnapshot.toObject(TeacherObj.class);
                        teacherName.setText(teacherObj.getName());
                        schoolName.setText(teacherObj.getSchool());
                        Glide.with(ViewTeacherProfile.this).load(teacherObj.getProPicURL()).into(profilePic);
                        SubsRef
                                .whereEqualTo("studentId",user.getPhoneNumber())
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.size() == 1){
                                    subscribe.setText("SUBSCRIBED");
                                    subscribe.setTextColor(Color.parseColor("#eac7c7"));
                                    loadingPopup.dialogDismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewTeacherProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewTeacherProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
                    progress.dismiss();
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
                    progress.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
        Sref = FirebaseFirestore.getInstance().collection(
                "/TEACHERS/" +
                        teacher_ID +
                        "/SUBSCRIBERS"
        );
        Sref.whereEqualTo("StudentId",user.getPhoneNumber())//TODO:compare with student ID to check if they subscribed
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() == 1){
                            isSubscriber = true;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewTeacherProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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