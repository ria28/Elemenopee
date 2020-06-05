package org.wazir.build.elemenophee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.IntLogSigScreens.ChooseAdapter;
import org.wazir.build.elemenophee.IntLogSigScreens.ChooseMoObj;
import org.wazir.build.elemenophee.Interfaces.ChooseEveHandler;
import org.wazir.build.elemenophee.Student.StudentMainAct;
import org.wazir.build.elemenophee.Teacher.mainDashBoardTeacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpUserActivity extends AppCompatActivity implements ChooseEveHandler {
    ArrayList<ChooseMoObj> classes;
    ArrayList<ChooseMoObj> subjects;
    private static int TO_TEACHER = 0, TO_STUDENT = 1;
    RecyclerView teaRcvClas, teaRcvSub, stuRcvClas;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextInputLayout teachSchool, teachBio, teachExperience;
    TextInputLayout stuSchool, stuBio, stuTarget;
    ImageView signupTeacher, signupStudent;
    ProgressBar bar;
    String phoneNumber;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        teachSchool = findViewById(R.id.textInputLayout4);
        teachBio = findViewById(R.id.textInputLayout6);
        teachExperience = findViewById(R.id.textInputLayout7);
        stuSchool = findViewById(R.id.textInputLayout2);
        stuBio = findViewById(R.id.textInputLayout3);
        stuTarget = findViewById(R.id.textInputLayout8);
        signupTeacher = findViewById(R.id.imageView4);
        signupStudent = findViewById(R.id.imageView5);
        bar = findViewById(R.id.progressBar2);

        classes = new ArrayList<>();
        subjects = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        signupStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpStudent();
                updateUi(true);
            }
        });

        signupTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpTeacher();
                updateUi(true);
            }
        });

        // setup RecyclerViews
        setupRcviews();
        phoneNumber = getIntent().getStringExtra("PHONE");
    }

    void signUpTeacher() {
        String ts, tb, te;
        ts = teachSchool.getEditText().getText().toString();
        tb = teachBio.getEditText().getText().toString();
        te = teachExperience.getEditText().getText().toString();


        ArrayList<String> subject_string = new ArrayList<>();
        for (ChooseMoObj obj : subjects) {
            subject_string.add(obj.getText());
        }

        ArrayList<Integer> classes_string = new ArrayList<>();
        for (ChooseMoObj obj : classes) {
            classes_string.add(obj.getClas());
        }

        Map<String, Object> user = new HashMap<>();
        user.put("TEA_SCHOOL", ts);
        user.put("TEA_BIO", tb);
        user.put("TEA_EXP", te);
        user.put("TEA_CLASSES", classes_string);
        user.put("TEA_SUBS", subject_string);
        user.put("CONTACT", phoneNumber);
        db.collection("TEACHERS")
                .document(phoneNumber)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        navigate(TO_TEACHER);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpUserActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUi(false);
                    }
                });
    }

    void signUpStudent() {
        String ss, sb, se;
        ss = stuSchool.getEditText().getText().toString();
        sb = stuBio.getEditText().getText().toString();
        se = stuTarget.getEditText().getText().toString();

        ArrayList<Integer> classes_string = new ArrayList<>();
        for (ChooseMoObj obj : classes) {
            classes_string.add(obj.getClas());
        }

        Map<String, Object> user = new HashMap<>();
        user.put("STU_SCHOOL", ss);
        user.put("STU_BIO", sb);
        user.put("STU_TARGET", se);
        user.put("STU_CLASSES", classes_string);
        user.put("CONTACT", phoneNumber);

        db.collection("STUDENTS")
                .document(phoneNumber)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        navigate(TO_STUDENT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpUserActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUi(false);
                    }
                });
    }

    void setupRcviews() {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        ChooseAdapter adapter = new ChooseAdapter(this, getClas());
        ChooseAdapter adapter1 = new ChooseAdapter(this, getSubs());
        adapter1.setHandler(this);
        adapter.setHandler(this);
        teaRcvClas = findViewById(R.id.recyclerView123);
        teaRcvClas.setAdapter(adapter);
        teaRcvClas.setLayoutManager(manager);
        LinearLayoutManager manager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        teaRcvSub = findViewById(R.id.recyclerView12);
        teaRcvSub.setLayoutManager(manager1);
        teaRcvSub.setAdapter(adapter1);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        stuRcvClas = findViewById(R.id.recyclerView);
        stuRcvClas.setAdapter(adapter);
        stuRcvClas.setLayoutManager(manager2);
    }

    ArrayList<ChooseMoObj> getClas() {
        ArrayList<ChooseMoObj> objs = new ArrayList<>();
        objs.add(new ChooseMoObj(5));
        objs.add(new ChooseMoObj(6));
        objs.add(new ChooseMoObj(7));
        objs.add(new ChooseMoObj(8));
        objs.add(new ChooseMoObj(9));
        objs.add(new ChooseMoObj(10));
        objs.add(new ChooseMoObj(11));
        objs.add(new ChooseMoObj(12));
        return objs;
    }

    ArrayList<ChooseMoObj> getSubs() {
        ArrayList<ChooseMoObj> objs = new ArrayList<>();
        objs.add(new ChooseMoObj("English"));
        objs.add(new ChooseMoObj("Hindi"));
        objs.add(new ChooseMoObj("Maths"));
        objs.add(new ChooseMoObj("Chemistry"));
        objs.add(new ChooseMoObj("Physics"));
        objs.add(new ChooseMoObj("Social Sc."));
        objs.add(new ChooseMoObj("Geography"));
        objs.add(new ChooseMoObj("Biology"));
        objs.add(new ChooseMoObj("Accounts"));
        objs.add(new ChooseMoObj("B. St."));
        objs.add(new ChooseMoObj("Economics"));
        return objs;
    }

    void navigate(int direction) {
        if (direction == TO_TEACHER) {
            Intent intent = new Intent(SignUpUserActivity.this, mainDashBoardTeacher.class);
            intent.putExtra("CLASS_sig_up", classes);
            intent.putExtra("SUBS_sig_up", subjects);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SignUpUserActivity.this, StudentMainAct.class);
            intent.putExtra("CLASS_sign_up", classes);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void selection(ChooseMoObj param, boolean action) {
        if (action) {
            if (param.getClas() != -1) {
                classes.add(param);
            } else {
                subjects.add(param);
            }
        } else {
            if (param.getClas() != -1) {
                classes.remove(param);
            } else {
                subjects.remove(param);
            }
        }
    }

    void updateUi(boolean task) {
        if (task) {
            raiseDialog();
        } else {
            alertDialog.dismiss();
        }
    }

    public void raiseDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(SignUpUserActivity.this);
        final View view1 = getLayoutInflater().inflate(R.layout.layout_alert_progress, null);
        alert.setView(view1);
        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
