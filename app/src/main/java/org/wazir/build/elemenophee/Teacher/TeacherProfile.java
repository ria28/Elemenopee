package org.wazir.build.elemenophee.Teacher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.ProfilePicBottomModalSheet;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.ViewTeacherProfile;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfile extends AppCompatActivity implements ProfilePicBottomModalSheet.BottomSheetListener {

    private static final int PICK_PIC = 786;
    CircleImageView TeacherProfilePic;
    EditText name, school, bio;
    TextView subsribers;
    CardView changeEmail;
    ImageView saveProfile;
    TeacherObj obj;
    LoadingPopup loading;
    FirebaseAuth mAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference profileRef = FirebaseFirestore.getInstance().collection("TEACHERS");

    LoadingPopup loadingPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        init();


        TeacherProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePicBottomModalSheet sheet = new ProfilePicBottomModalSheet();
                sheet.show(getSupportFragmentManager(), "PROFILE_PIC_ACTIONS_BOTTOM_SHEET");
            }
        });

        loadingPopup.dialogRaise();

        profileRef
                .document(user.getPhoneNumber())
                .collection("SUBSCRIBERS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            subsribers.setText(String.valueOf(task.getResult().size()));
                        } else {
                            Toast.makeText(TeacherProfile.this, task.getException().getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        profileRef
                .document(Objects.requireNonNull(user.getPhoneNumber()))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                obj = documentSnapshot.toObject(TeacherObj.class);
                assert obj != null;
                name.setText(obj.getName());
                school.setText(obj.getSchool());
                bio.setText(obj.getBio());
                if (obj.getProPicURL() != null)
                    Glide.with(TeacherProfile.this).load(obj.getProPicURL()).into(TeacherProfilePic);
                else
                    Glide.with(TeacherProfile.this).load(getDrawable(R.drawable.profile_dummy)).into(TeacherProfilePic);
                loadingPopup.dialogDismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Glide.with(TeacherProfile.this).load(getDrawable(R.drawable.profile_dummy)).into(TeacherProfilePic);
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals(obj.getName()) && !name.getText().toString().isEmpty()) {
                    profileRef.document(user.getPhoneNumber()).update("name", name.getText().toString());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name.getText().toString())
                            .build();
                    mAuth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                if (!school.getText().toString().equals(obj.getSchool()) && !school.getText().toString().isEmpty()) {
                    profileRef.document(user.getPhoneNumber()).update("school", school.getText().toString());
                }
                if (!bio.getText().toString().equals(obj.getName()) && !bio.getText().toString().isEmpty()) {
                    profileRef.document(user.getPhoneNumber()).update("bio", bio.getText().toString());
                }
                finish();
            }
        });

    }

    private void init() {
        TeacherProfilePic = findViewById(R.id.TeacherProfilePicture);
        name = findViewById(R.id.TeacherProfileName);
        school = findViewById(R.id.TeacherProfileSchoolName);
        bio = findViewById(R.id.TeacherProfileBio);
        subsribers = findViewById(R.id.TeacherProfileConnections);
        changeEmail = findViewById(R.id.ChangeEmail);
        saveProfile = findViewById(R.id.SaveProfile);
        loadingPopup = new LoadingPopup(TeacherProfile.this);
        loading = new LoadingPopup(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void BtnClicked(String BtnText) {
        switch (BtnText) {
            case "ViewPic":
                final AlertDialog.Builder alert = new AlertDialog.Builder(TeacherProfile.this);
                final View view1 = LayoutInflater.from(TeacherProfile.this).inflate(R.layout.view_profile_pic_dialog, null);
                ImageView profilePic = view1.findViewById(R.id.view_profile_pic_dialog);
                if (obj.getProPicURL() == null)
                    Glide.with(TeacherProfile.this).load(getDrawable(R.drawable.profile_dummy)).into(profilePic);
                else
                    Glide.with(TeacherProfile.this).load(obj.getProPicURL()).into(profilePic);

                profilePic.setImageDrawable(getDrawable(R.drawable.img_intro_4));
                alert.setView(view1);
                AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                break;
            case "ChangePic":
                changePic();
                break;
            case "RemovePic":
                removePic();
                break;
        }
    }

    private void removePic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherProfile.this);
        builder.setMessage("Do you want to delete your profile picture");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        profileRef.document(user.getPhoneNumber()).update("proPicURL", null);
                        Glide.with(TeacherProfile.this).load(getDrawable(R.drawable.img_intro_4)).into(TeacherProfilePic);
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void changePic() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        @SuppressLint("IntentReset")
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_PIC);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_PIC) {
            loading.dialogRaise();
            Uri selectedPicPath = data.getData();
            StorageReference ref = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference();
            final StorageReference reference = ref.child("PROFILE_PICTURES/" + user.getPhoneNumber());

            reference.putFile(selectedPicPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                            profileRef.document(user.getPhoneNumber()).update("proPicURL", uri.toString());
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Glide.with(TeacherProfile.this).load(uri.toString()).into(TeacherProfilePic);
                                                loading.dialogDismiss();
                                            }
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}