package org.wazir.build.elemenophee.Student.StudentSubscription;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.ModelObj.SubscribedTOmodel;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class StudentSubsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<TeacherObj> list = new ArrayList<>();
    CollectionReference reference;
    CollectionReference subTea;
    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<String> subsList = new ArrayList<>();
    StuTeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subs);

        adapter = new StuTeacherAdapter(StudentSubsActivity.this, list);

        recyclerView = findViewById(R.id.teachers_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        reference = FirebaseFirestore.getInstance()
                .collection("TEACHERS");
        subTea = FirebaseFirestore.getInstance().collection("STUDENTS")
                .document(user.getPhoneNumber()).collection("SUBSCRIBED_TO");

        subTea
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            SubscribedTOmodel model = doc.toObject(SubscribedTOmodel.class);
                            subsList.add(model.getTeacherID());
                        }
                        if (subsList.size() > 0) {
                            reference
                                    .whereIn("phone", subsList)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {
                                                if (!task.getResult().isEmpty()) {
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        Log.d("TAG", "onSuccess: "+ doc.get("name"));
                                                        String name_ = doc.get("name").toString();
                                                        String description_ = doc.get("experience").toString();
                                                        String picUrl = doc.get("proPicURL").toString();
                                                        ArrayList<String> subjects_ = (ArrayList<String>) doc.get("subs");

                                                        list.add(new TeacherObj(name_, description_, picUrl, subjects_));
                                                        adapter.notifyDataSetChanged();
                                                    }

                                                }
                                                else
                                                    Log.d("TAG", "onComplete: ");
                                            } else
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentSubsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}