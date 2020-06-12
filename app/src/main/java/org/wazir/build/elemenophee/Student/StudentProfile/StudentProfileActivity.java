package org.wazir.build.elemenophee.Student.StudentProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

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
        Bitmap ImageBitmap= BitmapFactory.decodeByteArray(intent.getByteArrayExtra("byteArray"),0,intent.getByteArrayExtra("byteArray").length);
        profileImage.setImageBitmap(ImageBitmap);

//        if(getIntent().hasExtra("byteArray")) {
//            ImageView _imv= new ImageView(this);
//            Bitmap _bitmap = BitmapFactory.decodeByteArray(
//                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
//            _imv.setImageBitmap(_bitmap);
//        }
//        Log.d("classs", "onCreate: "+ others_);
//        String phone = intent.getStringExtra("PHONE_");
//        ArrayList<Integer> Classs = intent.getIntegerArrayListExtra("CLASS_");

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
        String number = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        FirebaseFirestore.getInstance().collection("STUDENTS").document(number)
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

                        }
                    }
                });
    }
}