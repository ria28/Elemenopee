package org.wazir.build.elemenophee.Student.StuProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentMainAct;


import static org.wazir.build.elemenophee.Student.StuProfile.EditStuProfileActivity.KEY_NAME;
import static org.wazir.build.elemenophee.Student.StuProfile.EditStuProfileActivity.STU_BIO;
import static org.wazir.build.elemenophee.Student.StuProfile.EditStuProfileActivity.STU_SCHOOL;


public class ProfileMainActivity extends AppCompatActivity {

    String phone;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference detailRef = db.document("STUDENTS/" + phone);


    TextView name_tv;
    TextView school_tv;
    TextView bio_tv;

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

    @Override
    protected void onStart() {
        super.onStart();
        detailRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ProfileMainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString(KEY_NAME);
                    String school = documentSnapshot.getString(STU_SCHOOL);
                    String bio = documentSnapshot.getString(STU_BIO);

                    name_tv.setText(name);
                    school_tv.setText(school);
                    bio_tv.setText(bio);


                }
            }
        });
    }


}