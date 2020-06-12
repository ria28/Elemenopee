package org.wazir.build.elemenophee.Student.StuProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.wazir.build.elemenophee.R;

import java.util.HashMap;
import java.util.Map;

public class EditStuProfileActivity extends AppCompatActivity {

    public static final String KEY_NAME = "name";
    public static final String STU_SCHOOL = "school";
    public static final String STU_BIO = "bio";
    String phone;

    private EditText editTextName;
    private EditText editTextSchool;
    private EditText editTextBio;
    private Button save;
    Boolean clicked =false;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference detailRef = db.document("STUDENTS/"+phone);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        phone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        editTextName=findViewById(R.id.name_edit);
        editTextSchool = findViewById(R.id.school_edit);
        editTextBio = findViewById(R.id.bio_edit);
        save = findViewById(R.id.save_edit);

    }

    @Override
    protected void onStart() {
        super.onStart();
        detailRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(EditStuProfileActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString(KEY_NAME);
                    String school = documentSnapshot.getString(STU_SCHOOL);
                    String bio= documentSnapshot.getString(STU_BIO);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            save(v);
                        }
                    });

                    if(clicked) {
                        Intent intent = new Intent(EditStuProfileActivity.this, ProfileMainActivity.class);
                        intent.putExtra("NAME", name);
                        intent.putExtra("SCHOOL", school);
                        intent.putExtra("BIO", bio);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    public void save(View v){
        String name = editTextName.getText().toString();
        String school= editTextSchool.getText().toString();
        String bio= editTextBio.getText().toString();

        Map<String, Object> student = new HashMap<>();
        student.put(KEY_NAME, name);
        student.put(STU_SCHOOL, school);
        student.put(STU_BIO,bio);


       detailRef.set(student)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditStuProfileActivity.this, "Details saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditStuProfileActivity.this, "Error! Not saved", Toast.LENGTH_SHORT).show();
                    }
                });

        clicked=true;

    }
}