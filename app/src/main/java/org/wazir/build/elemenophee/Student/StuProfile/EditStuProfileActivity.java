package org.wazir.build.elemenophee.Student.StuProfile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SignUpUserActivity;
import org.wazir.build.elemenophee.Student.StudentProfile.StudentProfileActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditStuProfileActivity extends AppCompatActivity {

    //    public static final String KEY_NAME = "name";
//    public static final String STU_SCHOOL = "school";
//    public static final String STU_BIO = "bio";
    static final int REQUEST_TAKE_PHOTO = 0;
    String phone;
    static String bio;

    private EditText editTextName;
    private EditText editTextSchool;
    private EditText editTextBio;
    private EditText editTextTarget;
    private TextView others;
    private ImageView cam,profileImage;
    ArrayList<Integer> Classs;
    private Button save;
    Boolean clicked = false;

    String name;
    String school;
    String target;
    String text;
    Context context;
    String currentPhotoPath;
    Bitmap imageBitmap;

//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private DocumentReference detailRef = db.document("STUDENTS/" + phone);

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
        profileImage=findViewById(R.id.profile_image);


        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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

    private void dispatchTakePictureIntent() {
        
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
////                // Error occurred while creating the File
////
//            }
////            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
//            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            profileImage.setImageBitmap(imageBitmap);

    }


//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
////         Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

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


        String number = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        FirebaseFirestore.getInstance().collection("STUDENTS").document(number).set(obj)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditStuProfileActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditStuProfileActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        if (clicked) {
            Intent intent2 = new Intent(EditStuProfileActivity.this, StudentProfileActivity.class);
            intent2.putExtra("NAME_", name);
            intent2.putExtra("SCHOOL_", school);
            intent2.putExtra("BIO_", bio);
            intent2.putExtra("TARGET_", target);
            intent2.putExtra("OTH_", text);
            ByteArrayOutputStream _bs = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,50,_bs);
            intent2.putExtra("byteArray", _bs.toByteArray());


//            intent2.putExtra("PHONE_", phone);
//            intent2.putExtra("CLASS_", Classs);
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

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        detailRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if (e != null) {
//                    Toast.makeText(EditStuProfileActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (documentSnapshot.exists()) {
//                    String name = documentSnapshot.getString(KEY_NAME);
//                    String school = documentSnapshot.getString(STU_SCHOOL);
//                    String bio= documentSnapshot.getString(STU_BIO);
//
//                    save.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            save(v);
//                        }
//                    });
//
//                    if(clicked) {
//                        Intent intent = new Intent(EditStuProfileActivity.this, ProfileMainActivity.class);
//                        intent.putExtra("NAME", name);
//                        intent.putExtra("SCHOOL", school);
//                        intent.putExtra("BIO", bio);
//                        startActivity(intent);
//                    }
//
//                }
//            }
//        });
//    }
//
//    public void save(View v){
//        String name = editTextName.getText().toString();
//        String school= editTextSchool.getText().toString();
//        String bio= editTextBio.getText().toString();
//
//        Map<String, Object> student = new HashMap<>();
//        student.put(KEY_NAME, name);
//        student.put(STU_SCHOOL, school);
//        student.put(STU_BIO,bio);
//
//
//       detailRef.set(student)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(EditStuProfileActivity.this, "Details saved", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(EditStuProfileActivity.this, "Error! Not saved", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        clicked=true;
//
//    }
}