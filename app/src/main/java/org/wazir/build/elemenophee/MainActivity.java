package org.wazir.build.elemenophee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.Student.StudentMainAct;
import org.wazir.build.elemenophee.Teacher.TeacherMainActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView stateVer;
    String mVerificationId;
    boolean FLAG;

    TextInputLayout log_email, log_pass, sig_email, sig_pass, sig_phone, sig_otp;
    ImageView login_user, signup_user;
    CardView verifyOtp;

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
        verifyOtp = findViewById(R.id.verify_otp);
        stateVer = findViewById(R.id.state_verification);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FLAG = false;

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
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateVer.getText().equals("Verify Phone Number")) {
                    sendOtp("+91 " + sig_phone.getEditText().getText().toString().trim());
                    stateVer.setText("VERIFY");
                } else {
                    signinUser(sig_otp.getEditText().getText().toString());
                    stateVer.setText("Verify Phone Number");
                }
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
        if (FLAG) {
            final String email = sig_email.getEditText().getText().toString(), pass = sig_pass.getEditText().getText().toString();
            if (!email.equals("") && !pass.equals(""))
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                navigate(2);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                            }
                        });
        } else {
            Toast.makeText(this, "Phone Number Not Verified", Toast.LENGTH_LONG).show();
        }
    }

    void navigate(int routine) {
        switch (routine) {
            case (1):
                db.collection("TEACHERS")
                        .document(mAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Intent intent = new Intent(MainActivity.this, TeacherMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                db.collection("STUDENTS")
                        .document(mAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                startActivity(new Intent(MainActivity.this, StudentMainAct.class));
                                finish();
                            }
                        });
                break;
            case (2):
                Intent intent = new Intent(MainActivity.this, SignUpUserActivity.class);
                intent.putExtra("PHONE", sig_phone.getEditText().getText().toString());
                startActivity(intent);
                finish();
                break;
        }
    }



    void sendOtp(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            signinUser(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(MainActivity.this, "Invalid Phone Number", Toast.LENGTH_LONG).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(MainActivity.this, "Wait Before Trying again", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId = verificationId;
            Toast.makeText(MainActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
        }
    };

    void signinUser(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FLAG = true;
                    }
                });
    }

    void signinUser(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signinUser(credential);
    }
}
