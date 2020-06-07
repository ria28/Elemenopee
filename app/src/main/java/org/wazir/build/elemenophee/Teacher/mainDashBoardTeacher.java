package org.wazir.build.elemenophee.Teacher;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SplashScreen;
import org.wazir.build.elemenophee.Utils.PermissionUtil;

public class mainDashBoardTeacher extends AppCompatActivity implements PermissionUtil.PermissionsCallBack {

    ConstraintLayout upload_card;
    ConstraintLayout live_lecture_card;
    ConstraintLayout view_upload_card;
    CardView logoutUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board_teacher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        init();
        upload_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainDashBoardTeacher.this, UploadActivity.class));
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
    }
}
