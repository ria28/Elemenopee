package org.wazir.build.elemenophee.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.ProfilePicBottomModalSheet;
import org.wazir.build.elemenophee.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfile extends AppCompatActivity implements ProfilePicBottomModalSheet.BottomSheetListener {

    CircleImageView TeacherProfilePic;
    EditText name, school, bio;
    TextView subsribers;
    CardView addPayDetail,changeEmail;
    ImageView saveProfile;

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
                sheet.show(getSupportFragmentManager(),"PROFILE_PIC_ACTIONS_BOTTOM_SHEET");
            }
        });

        loadingPopup.dialogRaise();
        profileRef
                .document(user.getPhoneNumber())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TeacherObj obj = documentSnapshot.toObject(TeacherObj.class);
                name.setText(obj.getName());
                school.setText(obj.getSchool());
                bio.setText(obj.getBio());
                loadingPopup.dialogDismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        TeacherProfilePic = findViewById(R.id.TeacherProfilePicture);
        name = findViewById(R.id.TeacherProfileName);
        school = findViewById(R.id.TeacherProfileSchoolName);
        bio = findViewById(R.id.TeacherProfileBio);
        subsribers = findViewById(R.id.TeacherProfileConnections);
        addPayDetail = findViewById(R.id.AddPayDetails);
        changeEmail = findViewById(R.id.ChangeEmail);
        saveProfile = findViewById(R.id.SaveProfile);
        loadingPopup = new LoadingPopup(TeacherProfile.this);
    }

    @Override
    public void BtnClicked(String BtnText) {
        switch (BtnText){
            case "ViewPic":
                final AlertDialog.Builder alert = new AlertDialog.Builder(TeacherProfile.this);
                final View view1 = LayoutInflater.from(TeacherProfile.this).inflate(R.layout.view_profile_pic_dialog, null);
                ImageView profilePic = view1.findViewById(R.id.view_profile_pic_dialog);
                profilePic.setImageDrawable(getDrawable(R.drawable.img_intro_4));
                alert.setView(view1);
                AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                break;
            case "ChangePic": //TODO:update profile Attribute
                break;
            case "RemovePic": //TODO: delete profile attribute
                break;

        }
    }
}