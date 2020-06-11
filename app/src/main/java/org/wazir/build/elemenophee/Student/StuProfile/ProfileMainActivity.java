package org.wazir.build.elemenophee.Student.StuProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentMainPanel.StudentMainAct;





public class ProfileMainActivity extends AppCompatActivity {

    String phone;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private DocumentReference detailRef = db.document("STUDENTS/" + phone);


    TextView name_tv;
    TextView school_tv;
    TextView bio_tv;
    ImageView profileImage;

    StudentMainAct studentMainAct=new StudentMainAct();
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileMainActivity.this, StudentMainAct.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        phone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        name_tv = findViewById(R.id.name_tv);
        school_tv = findViewById(R.id.school_tv);
        bio_tv = findViewById(R.id.tv_bio);
        Button edit = findViewById(R.id.edit);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMainActivity.this, EditStuProfileActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String school = intent.getStringExtra("SCHOOL");
        String bio = intent.getStringExtra("BIO");


        name_tv.setText(name);
        school_tv.setText(school);
        bio_tv.setText(bio);

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
                            name_tv.setText(obj.getName());
                            school_tv.setText(obj.getSchool());
                            String bio = obj.getBio()+"\nCLASSES :"+obj.getClasses()+"\nCONTACT :"+ obj.getContact()+"\nTARGET :"+ obj.getTarget();
                            bio_tv.setText(bio);

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
       getStudentData();
    }


}