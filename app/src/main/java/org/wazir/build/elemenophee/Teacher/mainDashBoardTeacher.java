package org.wazir.build.elemenophee.Teacher;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SplashScreen;
import org.wazir.build.elemenophee.Utils.PermissionUtil;

import java.util.ArrayList;

public class mainDashBoardTeacher extends AppCompatActivity implements PermissionUtil.PermissionsCallBack {

    ConstraintLayout upload_card;
    ConstraintLayout live_lecture_card;
    ConstraintLayout view_upload_card;
    CardView logoutUser;
    TextView name, designation;
    ArrayList<String> classes, subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board_teacher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        init();
        getTeacherInfo();
        upload_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainDashBoardTeacher.this, UploadActivity.class);
                intent.putExtra("CLASSES", classes);
                intent.putExtra("SUBS", subjects);
                startActivity(intent);
            }
        });

        view_upload_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainDashBoardTeacher.this, viewUploadActivity.class));
            }
        });
        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(mainDashBoardTeacher.this, SplashScreen.class));
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }

    @Override
    public void permissionsGranted() {
        Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void permissionsDenied() {
        Toast.makeText(this, "Permissions Denied!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkAndRequestPermissions(this,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("MYTAG", "Permissions are granted. Good to go!");
            }
        }
    }

    void init() {
        upload_card = findViewById(R.id.upload_card);
        live_lecture_card = findViewById(R.id.live_lecture_card);
        view_upload_card = findViewById(R.id.view_upload_card);
        logoutUser = findViewById(R.id.cardView7);
        name = findViewById(R.id.textView22);
        designation = findViewById(R.id.textView23);
    }

    void getTeacherInfo() {
        String number = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        FirebaseFirestore.getInstance()
                .collection("TEACHERS")
                .document(number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            TeacherObj obj = task.getResult().toObject(TeacherObj.class);
                            // TODO: 6/7/2020 Here just Take the Classes and Subjects
                            name.setText(obj.getName());
                            designation.setText("TEACHER");

                            classes = new ArrayList<>();
                            for (int i : obj.getClasses()) {
                                classes.add("Class " + Integer.toString(i));
                            }
                            subjects = obj.getSubs();
                        }
                    }
                });
    }
}
