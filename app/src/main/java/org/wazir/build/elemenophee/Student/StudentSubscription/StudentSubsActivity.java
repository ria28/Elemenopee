package org.wazir.build.elemenophee.Student.StudentSubscription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class StudentSubsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<TeacherObj> list = new ArrayList<>();
    CollectionReference reference;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subs);

        recyclerView = findViewById(R.id.teachers_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        reference = FirebaseFirestore.getInstance().collection("TEACHERS");

        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String name_ = doc.get("name").toString();
                        String description_ = doc.get("experience").toString();
                        ArrayList<String> subjects_ = (ArrayList<String>) doc.get("subs");
                        list.add(new TeacherObj(name_, description_, subjects_));
                        recyclerView.setAdapter(new StuTeacherAdapter(list));
                    }
                } else
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}