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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.wazir.build.elemenophee.R;


public class UploadActivity extends AppCompatActivity {

    ImageView thumbnail;
    Button uploadBtn;
    Spinner classD, subjectD;
    TextView header;
    ProgressBar progressBar;


    private static final int PICK_FILE = 101;
    Uri selectedFilePath;

    private StorageReference ref;
    private String absolutePath;

    ArrayAdapter<String> classSpinnerAdapter;
    ArrayAdapter<String> subjectSpinnerAdapter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String fileType = getIntent().getStringExtra("FILE_TYPE");
        Toast.makeText(getApplicationContext(), fileType, Toast.LENGTH_SHORT).show();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);



        init();

        header.setText("Select " + fileType);

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {


                if (fileType.equals("VIDEO")) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("video/*");


                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("video/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, PICK_FILE);
                } else if (fileType.equals("PDF")) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("application/pdf");


                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("application/pdf");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, PICK_FILE);
                }


            }
        });


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                StorageReference reference = ref.child("random");
                reference.putFile(selectedFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        int progress = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        try {
                            progressBar.setProgress(progress);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }

    //TODO : Create thumbnail for api lower than 27
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_FILE) {

            selectedFilePath = data.getData();
            Glide.with(UploadActivity.this)
                    .load(selectedFilePath)
                    .into(thumbnail);

        }
    }


    void init() {
        thumbnail = findViewById(R.id.Upload_thumbnail);
        uploadBtn = findViewById(R.id.uploadBtn);
        header = findViewById(R.id.header_Upload_activity);

        classD = findViewById(R.id.classSpinner);
        subjectD = findViewById(R.id.subjectSpinner);

        classSpinnerAdapter = new ArrayAdapter<>(UploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Class 6", "Class 7", "Class 8"});

        subjectSpinnerAdapter = new ArrayAdapter<>(UploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"English", "Maths", "Science"});


        classD.setAdapter(classSpinnerAdapter);
        subjectD.setAdapter(subjectSpinnerAdapter);


        ref = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference();

    }


}
