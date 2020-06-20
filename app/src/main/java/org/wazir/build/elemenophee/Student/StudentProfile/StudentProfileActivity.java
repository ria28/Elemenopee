package org.wazir.build.elemenophee.Student.StudentProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StuProfile.EditStuProfileActivity;
import org.wazir.build.elemenophee.Student.StudentMainPanel.StudentMainAct;

import java.util.ArrayList;

public class StudentProfileActivity extends AppCompatActivity {

    TextView name, school, bio, target, others;
    ImageView profileImage;
    Button edit;
    String phone, Text;
    ArrayList<Integer> Classs = new ArrayList<>();
    FirebaseAuth mAuth;
    String url_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        school = findViewById(R.id.school);
        bio = findViewById(R.id.bio);
        target = findViewById(R.id.target);
        others = findViewById(R.id.others);
        edit = findViewById(R.id.edit2);
        profileImage=findViewById(R.id.profile_image_view);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, EditStuProfileActivity.class);
                intent.putExtra("NAME", name.getText());
                intent.putExtra("SCHOOL", school.getText());
                intent.putExtra("BIO", bio.getText());
                intent.putExtra("TARGET", target.getText());
//                intent.putExtra("OTH",Text);
                intent.putExtra("PHONE", phone);
                intent.putExtra("CLASS", Classs);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String name_ = intent.getStringExtra("NAME_");
        String school_ = intent.getStringExtra("SCHOOL_");
        String bio_ = intent.getStringExtra("BIO_");
        String target_ = intent.getStringExtra("TARGET_");
        String others_= intent.getStringExtra("OTH_");
        url_ = intent.getStringExtra("imageName");

        name.setText(name_);
        school.setText(school_);
        bio.setText(bio_);
        target.setText(target_);
        others.setText(others_);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StudentProfileActivity.this, StudentMainAct.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getStudentData();

    }

    void getStudentData() {
        FirebaseUser curUser = mAuth.getCurrentUser();
        if (curUser == null) {
            return;
        }
        String number = mAuth.getCurrentUser().getPhoneNumber().substring(3);
        FirebaseFirestore.getInstance().collection("STUDENTS").document("+91"+number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            StudentObj obj = task.getResult().toObject(StudentObj.class);
                            // TODO: 6/7/2020 Do what ever you want With the Student DATA here
                            name.setText(obj.getName());
                            school.setText(obj.getSchool());
                            Text = "\nCLASSES :" + obj.getClasses() + "\nCONTACT :" + obj.getContact();
                            bio.setText(obj.getBio());
                            others.setText(Text);                                           // cannot change-------------------------------------------------
                            target.setText(obj.getTarget());
                            phone = obj.getContact();
                            Classs = obj.getClasses();

                            DocumentSnapshot document = task.getResult();
                            String url= document.getString("imageUrl");
                            Glide.with(profileImage.getContext()).load(url).into(profileImage);
//                            Picasso.get().load(url).placeholder(R.mipmap.student1).fit().centerCrop().into(profileImage);

                        }
                    }
                });

    }
}