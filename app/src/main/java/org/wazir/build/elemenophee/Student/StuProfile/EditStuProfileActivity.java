package org.wazir.build.elemenophee.Student.StuProfile;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditStuProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSchool;
    private EditText editTextBio;
    private EditText editTextTarget;
    private TextView others;
    private ImageView cam;
    CircleImageView profileImage;
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
    LoadingPopup loader;

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
        loader = new LoadingPopup(this);
        Classs = new ArrayList<>();
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
                loader.dialogRaise();
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
        }).addOnFailureListener(e -> {
            // error ocurred
        });
    }

    private void uploadFile() {

        deleteImages();

        mStorageRef = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference("uploads");

        String number = mAuth.getCurrentUser().getPhoneNumber().substring(3);
//        System.currentTimeMillis()
        if (mImageUri != null) {
            fileReference = mStorageRef.child(number + ".png");

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {
                                            imageUrl = uri.toString();
                                            String number = mAuth.getCurrentUser().getPhoneNumber();
                                            FirebaseFirestore.getInstance()
                                                    .collection("STUDENTS")
                                                    .document(number)
                                                    .update("mImageUrl", imageUrl);
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setPhotoUri(uri)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            loader.dialogDismiss();
                                                            onBackPressed();
                                                        } else {
                                                            Toast.makeText(EditStuProfileActivity.this, "Failed To Update", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
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
        obj.setContact(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("STUDENTS")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        StudentObj obj = task.getResult().toObject(StudentObj.class);
                        editTextName.setText(obj.getName());
                        editTextSchool.setText(obj.getSchool());
                        editTextBio.setText(obj.getBio());
                        editTextTarget.setText(obj.getTarget());
                        Glide.with(getApplicationContext()).load(obj.getmImageUrl()).into(profileImage);
                        Classs = obj.getClasses();
                    }
                });
    }
}