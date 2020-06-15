package org.wazir.build.elemenophee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
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
    notesRecyclerAdapter notesAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressDialog progress;

    Spinner FileType;

    ArrayList<contentModel> videoList = new ArrayList<>();
    ArrayList<contentModel> pdfList = new ArrayList<>();

    CollectionReference reference;

    ArrayAdapter<String> FileTypeSpinnerViewAdapter;
    ArrayList<String> content = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        reference = FirebaseFirestore.getInstance().collection(
                "/TEACHERS/" +
                        user.getPhoneNumber() + //TODO:ADD Teacher id in Student View
                        "/RECENT_UPLOADS"
        );

        init();

        content.add("VIDEOS");
        content.add("NOTES");


        FileTypeSpinnerViewAdapter = new ArrayAdapter<>(ViewTeacherProfile.this,
                android.R.layout.simple_spinner_dropdown_item, content);

        FileType.setAdapter(FileTypeSpinnerViewAdapter);


        progress = new ProgressDialog(this);
        progress.setMessage("Retreiving data..");
        progress.setIndeterminate(true);

        progress.show();

        videoAdapter = new videoRecyclerAdapter(ViewTeacherProfile.this, false, videoList, this, -1);
        notesAdapter = new notesRecyclerAdapter(ViewTeacherProfile.this, pdfList);

        loadData("VIDEOS");
        loadData("NOTES");

        FileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(videoList.size()<=0){
                    Toast.makeText(getApplicationContext(),"No Recent Video Uploaded",Toast.LENGTH_SHORT).show();
                }
                if(pdfList.size()<=0){
                    Toast.makeText(getApplicationContext(),"No Recent Notes Uploaded",Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(ViewTeacherProfile.this,PaymentActivity.class));
            }
        });

    }

    private void setUpRecyclerView() {

        if (FileType.getSelectedItem().toString() == "VIDEOS")
            recyclerView.setAdapter(videoAdapter);
        else
            recyclerView.setAdapter(notesAdapter);
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
                        videoList.add(temp);
                        videoAdapter.notifyDataSetChanged();
                    }
                    Log.d("TAG", "onSuccess: " + videoList.size());
                } else {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        contentModel temp = doc.toObject(contentModel.class);
                        pdfList.add(temp);
                        notesAdapter.notifyDataSetChanged();
                    }
                    progress.dismiss();
                    Log.d("TAG", "onSuccess: " + pdfList.size());
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
    }

    @Override
    public void onClicked(int i, boolean isVideoPlaying) {
        Intent intent = new Intent(ViewTeacherProfile.this, videoPlayingActivity.class);
        intent.putExtra("VIDEO_LINK", videoList.get(i).getFileUrl());
        intent.putExtra("VIDEO_LIST", videoList);
        intent.putExtra("PDF_LIST", pdfList);
        intent.putExtra("FROM_RECENT", true);
        startActivity(intent);
    }

}