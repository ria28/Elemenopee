package org.wazir.build.elemenophee;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextInputLayout log_email, log_pass, sig_email, sig_pass, sig_phone, sig_otp;
    ImageView login_user, signup_user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        log_email = findViewById(R.id.id_li_mail);
        log_pass = findViewById(R.id.id_li_pass);
        sig_email = findViewById(R.id.id_su_mail);
        sig_pass = findViewById(R.id.id_su_pass);
        sig_phone = findViewById(R.id.id_su_phone);
        sig_otp = findViewById(R.id.id_su_otp);
        login_user = findViewById(R.id.imageView);
        signup_user = findViewById(R.id.imageView1);

        mAuth = FirebaseAuth.getInstance();

        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        signup_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    void loginUser() {
        final String email, pass;
        email = log_email.getEditText().getText().toString();
        pass = log_pass.getEditText().getText().toString();
        if (email.equals("") || pass.equals("")) {
            Toast.makeText(MainActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            navigate(1);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed To Login User", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    void signUpUser() {
        final String email = sig_email.getEditText().getText().toString(), pass = sig_pass.getEditText().getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (email.equals("") || pass.equals("")) {
                            Toast.makeText(MainActivity.this, "Please Enter Valid Data", Toast.LENGTH_LONG).show();
                        } else {
                            navigate(2);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void navigate(int routine) {
        switch (routine) {
            case (1):
                // TODO: 5/21/2020 navigate user to its screen Teacher Or Student
                break;
            case (2):
                // TODO: 5/21/2020 begin User SignUp for Student or Teacher
                break;
        }
    }

    void updateUi(int task) {
        switch (task) {

        }
    }


}
