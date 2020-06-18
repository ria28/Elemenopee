package org.wazir.build.elemenophee.Teacher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SplashScreen;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.otherAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.videoRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Utils.PermissionUtil;
import org.wazir.build.elemenophee.ViewTeacherProfile;

import java.util.ArrayList;

public class mainDashBoardTeacher extends AppCompatActivity implements PermissionUtil.PermissionsCallBack, videoRecyclerAdapter.onLayoutClick {

    private static final int PICK_VIDEO = 101;
    private static final int PICK_PDF = 102;
    private static final int PICK_FILE = 103;
    ConstraintLayout live_lecture_card;
    ConstraintLayout view_upload_card;
    ImageView uploadVideo, uploadPdf, uploadFile;
    CardView logoutUser;
    CardView viewProfile;
    TextView name, designation, mainPageName;
    ArrayList<String> classes, subjects;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;



    videoRecyclerAdapter videoAdapter;
    otherAdapter otherAdapter;
    notesRecyclerAdapter notesAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressDialog progress;


    Spinner FileType;

    ArrayList<contentModel> videoList = new ArrayList<>();
    ArrayList<contentModel> pdfList = new ArrayList<>();
    ArrayList<contentModel> otherList = new ArrayList<>();


    CollectionReference reference;

    String UploadType;
    ArrayAdapter<String> FileTypeSpinnerViewAdapter;
    ArrayList<String> content = new ArrayList<>();
    private Uri selectedFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board_teacher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        reference = FirebaseFirestore.getInstance().collection(
                "/TEACHERS/" +
                        user.getPhoneNumber() + //TODO:ADD Teacher id in Student View
                        "/RECENT_UPLOADS"
        );

        init();
        getTeacherInfo();

        content.add("VIDEOS");
        content.add("NOTES");
        content.add("OTHER");

        FileTypeSpinnerViewAdapter = new ArrayAdapter<>(mainDashBoardTeacher.this,
                android.R.layout.simple_spinner_dropdown_item, content);

        FileType.setAdapter(FileTypeSpinnerViewAdapter);


        progress = new ProgressDialog(this);
        progress.setMessage("Retreiving data..");
        progress.setIndeterminate(true);

        progress.show();

        videoAdapter = new videoRecyclerAdapter(mainDashBoardTeacher.this, false, videoList, this, -1);
        notesAdapter = new notesRecyclerAdapter(mainDashBoardTeacher.this, pdfList);
        otherAdapter = new otherAdapter(mainDashBoardTeacher.this,otherList);

        loadData("VIDEOS");
        loadData("NOTES");
        loadData("OTHER");

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


        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadType = "VIDEOS";
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("video/*");


                @SuppressLint("IntentReset")
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_VIDEO);
            }
        });
        uploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadType = "NOTES";
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("application/pdf");


                @SuppressLint("IntentReset")
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("application/pdf");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select PDF");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_PDF);
            }
        });
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadType = "OTHER";
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("*/*");


                @SuppressLint("IntentReset")
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("*/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select PDF");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_FILE);
            }
        });

        view_upload_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainDashBoardTeacher.this, viewUploadActivity.class);
                intent.putExtra("CLASSES", classes);
                intent.putExtra("SUBS", subjects);
                startActivity(intent);
            }
        });
        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(mainDashBoardTeacher.this, SplashScreen.class));
                finish();
            }
        });
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainDashBoardTeacher.this, ViewTeacherProfile.class));
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
                } else if(type == "NOTES"){
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        contentModel temp = doc.toObject(contentModel.class);
                        pdfList.add(temp);
                        notesAdapter.notifyDataSetChanged();
                    }
                    progress.dismiss();
                }
                else if(type == "OTHER"){
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        contentModel temp = doc.toObject(contentModel.class);
                        otherList.add(temp);
                        otherAdapter.notifyDataSetChanged();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }

    @Override
    public void permissionsGranted() {
        Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void permissionsDenied() {
        Toast.makeText(this, "Permissions Denied!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkAndRequestPermissions(this,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_VIDEO) {
                selectedFilePath = data.getData();
            } else if (requestCode == PICK_PDF) {
                selectedFilePath = data.getData();
            } else if (requestCode == PICK_FILE) {
                selectedFilePath = data.getData();
            }
            UploadDialog();
        }
    }

    private void UploadDialog() {
        uploadDialog dialog = new uploadDialog(mainDashBoardTeacher.this
                ,UploadType,classes,subjects,selectedFilePath,user);
        dialog.openDialog();
    }

    void init() {
        mainPageName = findViewById(R.id.mainDashBoardTeacherName);
        logoutUser = findViewById(R.id.logout);
        viewProfile = findViewById(R.id.ProfileTeacher);
        name = findViewById(R.id.textView22);
        designation = findViewById(R.id.textView23);
        mAuth = FirebaseAuth.getInstance();
        live_lecture_card = findViewById(R.id.LiveCardTeacher);
        view_upload_card = findViewById(R.id.viewUploadCardTeacher);
        FileType = findViewById(R.id.mainDashTeacherRecentSpinner);
        recyclerView = findViewById(R.id.recent_uploads_recycler);
        uploadVideo = findViewById(R.id.uploadVideo);
        uploadPdf = findViewById(R.id.uploadPdf);
        uploadFile = findViewById(R.id.uploadFile);

    }

    void getTeacherInfo() {

        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }
        String number = mAuth.getCurrentUser().getPhoneNumber();
        FirebaseFirestore.getInstance()
                .collection("TEACHERS")
                .document(number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            TeacherObj obj = task.getResult().toObject(TeacherObj.class);
                            // TODO: 6/7/2020 Here just Take the Classes and Subjects
                            mainPageName.setText("Hello\n"+ obj.getName());
                            name.setText(obj.getName());
                            designation.setText("TEACHER");

                            classes = new ArrayList<>();
                            for (int i : obj.getClasses()) {
                                classes.add("Class " + Integer.toString(i));
                            }
                            subjects = obj.getSubs();
                        }
                    }
                });
    }

    @Override
    public void onClicked(int i, boolean isVideoPlaying) {
        Intent intent = new Intent(mainDashBoardTeacher.this, videoPlayingActivity.class);
        intent.putExtra("VIDEO_LINK", videoList.get(i).getFileUrl());
        intent.putExtra("VIDEO_LIST", videoList);
        intent.putExtra("PDF_LIST", pdfList);
        intent.putExtra("OTHER_LIST", otherList);
        intent.putExtra("FROM_RECENT", true);
        intent.putExtra("IS_TEACHER", true);
        Log.d("TAG", "onClicked: " + videoList.get(i).getFileTitle());
        startActivity(intent);
    }
}

