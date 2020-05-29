package org.wazir.build.elemenophee.Teacher;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Utils.UploadUtil;


public class UploadActivity extends AppCompatActivity {

    Button uploadBtn,AddExisting,createNew;


    private static final int PICK_FILE = 101;
    Uri selectedFilePath;

    public static final String UPLOAD_UTIL = "UPLOAD_UTIL";


    ArrayAdapter<String> classSpinnerAdapter;
    ArrayAdapter<String> subjectSpinnerAdapter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String fileType = getIntent().getStringExtra("FILE_TYPE");
        Toast.makeText(getApplicationContext(), fileType, Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);






        init();


        AddExisting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {


                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("video/*");


                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("video/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, PICK_FILE);



            }
        });


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Data data =  new Data.Builder()
//                        .put(UPLOAD_UTIL, new UploadModel("Title","Description"))
                        .putString("fileURI",selectedFilePath.toString())
                        .build();

                 OneTimeWorkRequest request =  new OneTimeWorkRequest.Builder(UploadUtil.class)
                        .setInputData(data)
                        .build();

                WorkManager.getInstance().enqueue(request);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_FILE) {

            selectedFilePath = data.getData();
//            Glide.with(UploadActivity.this)
//                    .load(selectedFilePath)
//                    .into(thumbnail);

        }
    }


    void init() {
        uploadBtn = findViewById(R.id.videoUploadBtn);
        AddExisting = findViewById(R.id.addExisting);
        createNew = findViewById(R.id.createNew);
        classSpinnerAdapter = new ArrayAdapter<>(UploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Class 6", "Class 7", "Class 8"});

        subjectSpinnerAdapter = new ArrayAdapter<>(UploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"English", "Maths", "Science"});


    }


}
