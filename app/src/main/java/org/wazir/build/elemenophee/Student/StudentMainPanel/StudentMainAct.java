package org.wazir.build.elemenophee.Student.StudentMainPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SplashScreen;
import org.wazir.build.elemenophee.Student.StudentMainPanel.Objects.SingleHorizontal;
import org.wazir.build.elemenophee.Student.StudentMainPanel.Objects.SingleVertical;
import org.wazir.build.elemenophee.Student.StudentMainPanel.StuAdapter.Adapters.MainAdapter;

import java.util.ArrayList;

public class StudentMainAct extends AppCompatActivity {
    private ArrayList<Object> objects = new ArrayList<>();
    //    TextView textView = findViewById(R.id.heading);
    CardView cardLogout;
    TextView name, designation;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        cardLogout = findViewById(R.id.logout);
        name = findViewById(R.id.textView22);
        designation = findViewById(R.id.textView23);
        mAuth = FirebaseAuth.getInstance();
        getStudentData();
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(StudentMainAct.this, SplashScreen.class));
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_View);
        MainAdapter adapter = new MainAdapter(this, getObject());
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(StudentMainAct.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private ArrayList<Object> getObject() {
        objects.add(getVerticalData().get(0));
        objects.add(getHorizontalData().get(0));
        return objects;
    }

    @NonNull
    public static ArrayList<SingleVertical> getVerticalData() {
        ArrayList<SingleVertical> singleVerticals = new ArrayList<>();
        singleVerticals.add(new SingleVertical("Community", "Click here to view notes and study material available for students\nAn advanced Q/A Section for Student support", R.drawable.community));
        singleVerticals.add(new SingleVertical("Profile", "Edit or view profile here",  R.drawable.profile));
        return singleVerticals;
    }


    @NonNull
    public static ArrayList<SingleHorizontal> getHorizontalData() {
        ArrayList<SingleHorizontal> singleHorizontals = new ArrayList<>();
        singleHorizontals.add(new SingleHorizontal(R.drawable.lectures, "Lectures ", "last viewed lecture"));
        singleHorizontals.add(new SingleHorizontal(R.drawable.classroom, "Class Room", "Scheduled Lectures"));
        return singleHorizontals;
    }

    void getStudentData() {
        FirebaseUser curUser = mAuth.getCurrentUser();
        if (curUser == null) {
            return;
        }
        String number = mAuth.getCurrentUser().getPhoneNumber().substring(3);
        FirebaseFirestore.getInstance().collection("STUDENTS").document(number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            StudentObj obj = task.getResult().toObject(StudentObj.class);
                            // TODO: 6/7/2020 Do what ever you want With the Student DATA here
                            name.setText(obj.getName());
                            designation.setText("STUDENT");
                        }
                    }
                });
    }
}
