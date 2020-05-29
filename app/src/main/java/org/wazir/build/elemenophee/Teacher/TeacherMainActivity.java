package org.wazir.build.elemenophee.Teacher;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Utils.PermissionUtil;

public class TeacherMainActivity extends AppCompatActivity implements PermissionUtil.PermissionsCallBack {

    ConstraintLayout upload_card;
    ConstraintLayout live_lecture_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board_teacher);

        init();


        upload_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder alt = new AlertDialog.Builder(TeacherMainActivity.this);
                final LayoutInflater inflater = LayoutInflater.from(TeacherMainActivity.this);
                View view = inflater.inflate(R.layout.choose_upload_alert_dialog, null);
                alt.setView(view);
                final AlertDialog alertDialog = alt.create();

                TextView VIDEO = view.findViewById(R.id.VIDEOselector);
                TextView PDF = view.findViewById(R.id.PDFselector);

                VIDEO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeacherMainActivity.this, UploadActivity.class);
                        intent.putExtra("FILE_TYPE","VIDEO");
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                });
                PDF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeacherMainActivity.this, UploadActivity.class);
                        intent.putExtra("FILE_TYPE","PDF");
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
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
    }
}
