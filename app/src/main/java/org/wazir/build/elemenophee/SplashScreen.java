package org.wazir.build.elemenophee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.IntLogSigScreens.OnboardingActivity;
import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.Student.StuCommPanel.Stu_main_comm_screen;
import org.wazir.build.elemenophee.Teacher.mainDashBoardTeacher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            String number = mAuth.getCurrentUser().getPhoneNumber();
            db.collection("STUDENTS").document(number).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            StudentObj obj = task.getResult().toObject(StudentObj.class);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                            String expiryDate = obj.getExpiry();

                            Date date = null;
                            try {
                                date = df.parse(expiryDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date today = new Date();
                            if(today.after(date)){
                                Log.d("TAG", "onStart: "+ today + date);
                                startActivity(new Intent(SplashScreen.this,PaymentActivity.class));
                                finish();
                            }
                            else{
                            Intent intent = new Intent(SplashScreen.this, Stu_main_comm_screen.class);
                            intent.putExtra("STUDENT_CLASSES", obj.getClasses());
                            startActivity(intent);
                            finish();
                            }
                        }
                    });
            db.collection("TEACHERS").document(number).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            TeacherObj obj = task.getResult().toObject(TeacherObj.class);
                            Intent intent = new Intent(SplashScreen.this, mainDashBoardTeacher.class);
                            intent.putExtra("TEACHER_CLASSES", obj.getClasses());
                            startActivity(intent);
                            finish();
                        }
                    });
        } else {
            if (restorePrefData()) {
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
            } else {
                startActivity(new Intent(this, OnboardingActivity.class));
            }
            finish();
        }
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend", false);
    }
}