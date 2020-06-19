package org.wazir.build.elemenophee.Student.StuProfile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SignUpUserActivity;
import org.wazir.build.elemenophee.Student.StudentProfile.StudentProfileActivity;
import org.wazir.build.elemenophee.Teacher.model.contentModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditStuProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSchool;
    private EditText editTextBio;
    private EditText editTextTarget;
    private TextView others;
    private ImageView cam, profileImage;
    private Button save;

    Boolean clicked = false;

    ArrayList<Integer> Classs;
    String name;
    String school;
    String target;
    String text;
    String phone;
    static String bio;

    FirebaseAuth mAuth;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    StorageReference fileReference;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        phone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        editTextName = findViewById(R.id.name_edit);
        editTextSchool = findViewById(R.id.school_edit);
        editTextBio = findViewById(R.id.bio_edit);
        editTextTarget = findViewById(R.id.target_edit);
        others = findViewById(R.id.others_edit);
        save = findViewById(R.id.save_edit);
        cam = findViewById(R.id.camera);
        profileImage = findViewById(R.id.profile_image);
        mAuth = FirebaseAuth.getInstance();


        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
                saveDetails();
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(profileImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void deleteImages() {

        String number = mAuth.getCurrentUser().getPhoneNumber().substring(3);
        mStorageRef = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference("uploads");
        mStorageRef.child(number + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //file deleted
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error ocurred
            }
        });
    }

    private void uploadFile() {

        deleteImages();

        mStorageRef = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference("uploads");

        String number = mAuth.getCurrentUser().getPhoneNumber().substring(3);
//        System.currentTimeMillis()
        if (mImageUri != null) {
            fileReference = mStorageRef.child(number
                    + ".png");
//            + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {
                                            imageUrl = uri.toString();
                                            String number = mAuth.getCurrentUser().getPhoneNumber().substring(3);
                                            FirebaseFirestore.getInstance().collection("STUDENTS").document("+91" + number)
                                                    .update("imageUrl", imageUrl);
                                        }
                                    });
                            Toast.makeText(EditStuProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditStuProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDetails() {

        name = editTextName.getText().toString();
        school = editTextSchool.getText().toString();
        bio = editTextBio.getText().toString();
        target = editTextTarget.getText().toString();
        text = others.getText().toString();

        StudentObj obj = new StudentObj();
        obj.setName(name);
        obj.setSchool(school);
        obj.setBio(bio);
        obj.setTarget(target);
        obj.setContact(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        obj.setClasses(Classs);
        obj.setmImageUrl(imageUrl);


        FirebaseUser curUser = mAuth.getCurrentUser();
        if (curUser == null) {
            return;
        }
        String number = mAuth.getCurrentUser().getPhoneNumber().substring(3);

        FirebaseFirestore.getInstance().collection("STUDENTS").document("+91" + number).update(
                "name", name,
                "target", target,
                "bio", bio,
                "school", school,
                "classes", Classs,
                "imageUrl", imageUrl
        );

        uploadFile();

        if (clicked) {
            Intent intent2 = new Intent(EditStuProfileActivity.this, StudentProfileActivity.class);
            intent2.putExtra("NAME_", name);
            intent2.putExtra("SCHOOL_", school);
            intent2.putExtra("BIO_", bio);
            intent2.putExtra("TARGET_", target);
            intent2.putExtra("OTH_", text);
            intent2.putExtra("imageName", imageUrl);
            startActivity(intent2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        editTextName.setText(intent.getStringExtra("NAME"));
        editTextSchool.setText(intent.getStringExtra("SCHOOL"));
        bio = intent.getStringExtra("BIO");
        editTextBio.setText(bio);
        editTextTarget.setText(intent.getStringExtra("TARGET"));
        text = intent.getStringExtra("OTH");
        others.setText((intent.getStringExtra("OTH")));
        phone = intent.getStringExtra("PHONE");
        Classs = intent.getIntegerArrayListExtra("CLASS");
    }
}