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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.CommunitySection.ComPanActivity;
import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.ModelObj.SubscribersModel;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SplashScreen;
import org.wazir.build.elemenophee.Student.StudentSupport.ChatActivity;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.otherAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.recentSubscriberAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.videoRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Utils.PermissionUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class mainDashBoardTeacher extends AppCompatActivity implements PermissionUtil.PermissionsCallBack, videoRecyclerAdapter.onLayoutClick {

    private static final int PICK_VIDEO = 101;
    private static final int PICK_PDF = 102;
    private static final int PICK_FILE = 103;
    CardView live_lecture_card, sub_btn, search_teach;
    ConstraintLayout view_upload_card, communityCard;
    CardView uploadVideo, uploadPdf, uploadFile;
    CardView logoutUser;
    CardView viewProfile;

    TextView name, designation, mainPageName;
    ArrayList<String> classes, subjects;
    FirebaseAuth mAuth;
    RecyclerView recentContent, recentSubs;
    CircleImageView profilePic, proPic2;
    CardView editProfile;

    videoRecyclerAdapter videoAdapter;
    otherAdapter otherAdapter;
    notesRecyclerAdapter notesAdapter;
    recentSubscriberAdapter recentSubscriberAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressDialog progress;


    Spinner FileType;

    ArrayList<contentModel> videoList = new ArrayList<>();
    ArrayList<contentModel> pdfList = new ArrayList<>();
    ArrayList<contentModel> otherList = new ArrayList<>();
    ArrayList<StudentObj> subsribersList = new ArrayList<>();


    CollectionReference reference;

    String UploadType;
    ArrayAdapter<String> FileTypeSpinnerViewAdapter;
    ArrayList<String> content = new ArrayList<>();
    private Uri selectedFilePath;

    private CollectionReference studentRef;
    private CollectionReference subsRef;
    private ArrayList<String> subsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board_teacher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        reference = FirebaseFirestore.getInstance().collection("/TEACHERS/" + user.getPhoneNumber() + "/RECENT_UPLOADS");


        init();
        getTeacherInfo();
        FirebaseUser user = mAuth.getCurrentUser();

        Uri pictureUrl = user.getPhotoUrl();

        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(pictureUrl).into(profilePic);
            Glide.with(this).load(pictureUrl).into(proPic2);
        }

        content.add("VIDEOS");
        content.add("NOTES");
        content.add("OTHER");

        FileTypeSpinnerViewAdapter = new ArrayAdapter<>(mainDashBoardTeacher.this, android.R.layout.simple_spinner_dropdown_item, content);

        FileType.setAdapter(FileTypeSpinnerViewAdapter);

        progress = new ProgressDialog(this);
        progress.setMessage("Retreiving data..");
        progress.setIndeterminate(true);

        progress.show();

        videoAdapter = new videoRecyclerAdapter(mainDashBoardTeacher.this, false, videoList, this, -1, false);
        notesAdapter = new notesRecyclerAdapter(mainDashBoardTeacher.this, pdfList, false);
        otherAdapter = new otherAdapter(mainDashBoardTeacher.this, otherList, false);
        recentSubscriberAdapter = new recentSubscriberAdapter(subsribersList, mainDashBoardTeacher.this);

        loadData("VIDEOS");
        loadData("NOTES");
        loadData("OTHER");
        loadSubscriber();

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
        recentContent.setLayoutManager(layoutManager);
        recentContent.hasFixedSize();
        setUpRecyclerView();

        recentSubs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recentSubs.hasFixedSize();
        recentSubs.setAdapter(recentSubscriberAdapter);


        uploadVideo.setOnClickListener(v -> {
            UploadType = "VIDEOS";
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("video/*");


            @SuppressLint("IntentReset")
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("video/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_VIDEO);
        });
        uploadPdf.setOnClickListener(v -> {
            UploadType = "NOTES";
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("application/pdf");

            @SuppressLint("IntentReset")
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("application/pdf");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select PDF");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_PDF);
        });

        uploadFile.setOnClickListener(v -> {
            UploadType = "OTHER";
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("*/*");


            @SuppressLint("IntentReset")
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("*/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select PDF");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_FILE);
        });

        view_upload_card.setOnClickListener(v -> {
            Intent intent = new Intent(mainDashBoardTeacher.this, viewUploadActivity.class);
            intent.putExtra("CLASSES", classes);
            intent.putExtra("SUBS", subjects);
            startActivity(intent);
        });
        logoutUser.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(mainDashBoardTeacher.this, SplashScreen.class));
            finish();
        });
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainDashBoardTeacher.this, ChatActivity.class);
                intent.putExtra("SHOWBN", false);
                startActivity(intent);
            }
        });
    }

    private void loadSubscriber() {
        subsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            subsList = new ArrayList<>();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                SubscribersModel model = doc.toObject(SubscribersModel.class);
                subsList.add(model.getStudentID());
            }
            if (subsList.size() > 0) {

                studentRef.whereIn("contact", subsList)
                        .limit(10)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots1 -> {
                            if (queryDocumentSnapshots1.size() > 0) {
                                for (DocumentSnapshot doc : queryDocumentSnapshots1) {
                                    StudentObj model = doc.toObject(StudentObj.class);
                                    subsribersList.add(model);
                                    recentSubscriberAdapter.notifyDataSetChanged();
                                }
                            }
                        }).addOnFailureListener(e -> Toast.makeText(mainDashBoardTeacher.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).addOnFailureListener(e -> Toast.makeText(mainDashBoardTeacher.this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void setUpRecyclerView() {

        if (FileType.getSelectedItem().toString() == "VIDEOS")
            recentContent.setAdapter(videoAdapter);
        else
            recentContent.setAdapter(notesAdapter);
    }

    private void loadData(final String type) {
        reference.document("UPLOADS")
                .collection(type)
                .orderBy("timeStamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    switch (type) {
                        case "VIDEOS":
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                contentModel temp = doc.toObject(contentModel.class);
                                videoList.add(temp);
                                Log.d("TAG", "loadData: " + temp.getTeacherID());
                                videoAdapter.notifyDataSetChanged();
                            }
                            break;
                        case "NOTES":
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        contentModel temp = doc.toObject(contentModel.class);
                        pdfList.add(temp);
                        notesAdapter.notifyDataSetChanged();
                    }
                    progress.dismiss();
                    break;
                case "OTHER":
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        contentModel temp = doc.toObject(contentModel.class);
                        otherList.add(temp);
                        otherAdapter.notifyDataSetChanged();
                    }
                    progress.dismiss();
                    break;
            }
        }).addOnFailureListener(e -> {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        uploadDialog dialog = new uploadDialog(mainDashBoardTeacher.this, UploadType, classes, subjects, selectedFilePath, user);
        dialog.openDialog();
    }

    void init() {

        TextView editProfileTv = findViewById(R.id.id_setting123);
        editProfileTv.setText("EDIT PROFILE");

        TextView messages = findViewById(R.id.textView25);
        messages.setText("MESSAGES");

        editProfile = findViewById(R.id.to_settings);
        mainPageName = findViewById(R.id.mainDashBoardTeacherName);
        logoutUser = findViewById(R.id.to_logout);
        viewProfile = findViewById(R.id.to_subscriptions);
        name = findViewById(R.id.textView22);
        designation = findViewById(R.id.textView23);
        mAuth = FirebaseAuth.getInstance();
        live_lecture_card = findViewById(R.id.LiveCardTeacher);
        view_upload_card = findViewById(R.id.viewUploadCardTeacher);
        FileType = findViewById(R.id.mainDashTeacherRecentSpinner);
        recentContent = findViewById(R.id.recent_uploads_recycler);
        recentSubs = findViewById(R.id.recent_subscriber_recycler);
        uploadVideo = findViewById(R.id.uploadVideo);
        uploadPdf = findViewById(R.id.uploadPdf);
        uploadFile = findViewById(R.id.uploadFile);
        profilePic = findViewById(R.id.id_user_profile);
        proPic2 = findViewById(R.id.circleImageView);
        communityCard = findViewById(R.id.community_card);
        search_teach = findViewById(R.id.to_downloads);
        search_teach.setVisibility(View.GONE);

        communityCard.setOnClickListener(v -> {
            Intent intent = new Intent(mainDashBoardTeacher.this, ComPanActivity.class);
            intent.putExtra("SHOWBN", false);
            startActivity(intent);
        });


        studentRef = FirebaseFirestore.getInstance()
                .collection("STUDENTS");
        subsRef = FirebaseFirestore.getInstance().collection("TEACHERS")
                .document(user.getPhoneNumber()).collection("SUBSCRIBERS");

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(mainDashBoardTeacher.this, TeacherProfile.class);
            startActivity(intent);
        });
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        TeacherObj obj = task.getResult().toObject(TeacherObj.class);
                        mainPageName.setText(obj.getName());
                        name.setText(obj.getName());
                        designation.setText("TEACHER");

                        classes = new ArrayList<>();
                        for (int i : obj.getClasses()) {
                            classes.add("Class " + i);
                        }
                        subjects = obj.getSubs();
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

