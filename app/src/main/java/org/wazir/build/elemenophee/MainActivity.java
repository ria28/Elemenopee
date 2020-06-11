package org.wazir.build.elemenophee;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.Student.StudentMainPanel.StudentMainAct;
import org.wazir.build.elemenophee.Teacher.mainDashBoardTeacher;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextInputLayout phoneNoLi, otpLi;
    EditText nameSu, emailSu, phoneNoSu, otpSu, stateSu;
    CardView loginUser, signUpUser;
    LottieAnimationView verIcon;
    ProgressBar liPb, suPb;
    String verId, number;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button verifyBtnSu;
    LoadingPopup loadingPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        init();
    }

    void init() {
        phoneNoLi = findViewById(R.id.textInputLayout5);
        loadingPopup = new LoadingPopup(this);
        otpLi = findViewById(R.id.textInputLayout9);
        loginUser = findViewById(R.id.cardView10);
        nameSu = findViewById(R.id.editTextTextPersonName);
        emailSu = findViewById(R.id.editTextTextPersonName2);
        phoneNoSu = findViewById(R.id.editTextTextPersonName3);
        otpSu = findViewById(R.id.editTextTextPersonName4);
        stateSu = findViewById(R.id.editTextTextPersonName5);
        signUpUser = findViewById(R.id.cardView12);
        verIcon = findViewById(R.id.icon_verified);
        verifyBtnSu = findViewById(R.id.button);
        phoneNoLi.getEditText().addTextChangedListener(phNuChangeWatcher);
        phoneNoSu.addTextChangedListener(pnSuTw);
        liPb = findViewById(R.id.progressBar4);
        suPb = findViewById(R.id.progressBar3);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siWiOt(otpLi.getEditText().getText().toString(), null);
            }
        });
        signUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
                loadingPopup.dialogRaise();
            }
        });
        verifyBtnSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siWiOtSu(otpSu.getText().toString(), null);
            }
        });
    }

    private TextWatcher phNuChangeWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() == 10) {
                sendOtp(s.toString());
                liPb.setVisibility(View.VISIBLE);
            } else {
                // TODO: 6/10/2020 Do Nothing Here
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher pnSuTw = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() == 10) {
                sendOtpSu(s.toString());
                suPb.setVisibility(View.VISIBLE);
            } else {
                // TODO: 6/10/2020 Do Nothing Here
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    void sendOtp(String phoneNumber) {
        number = phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,
                mCallbacks// Activity (for callback binding)
        );
    }

    void sendOtpSu(String phoneNumber) {
        number = phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,
                mCallbacksSu// Activity (for callback binding)
        );
    }

    void siWiOt(String otp, PhoneAuthCredential cred) {
        PhoneAuthCredential credential;
        if (cred == null && otp != null) {
            credential = PhoneAuthProvider.getCredential(verId, otp);
        }
        credential = cred;

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            stuOrTea();
                        } else {
                            liPb.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    void siWiOtSu(String otp, PhoneAuthCredential cred) {
        PhoneAuthCredential credential;
        if (cred == null && otp != null) {
            credential = PhoneAuthProvider.getCredential(verId, otp);
        }
        credential = cred;

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    verIcon.setProgress(animation.getAnimatedFraction());
                                }
                            });
                            animator.start();
                        } else {
                            liPb.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    void stuOrTea() {
        if (number == null) {
            return;
        }
        db.collection("TEACHERS")
                .document(number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            navigate("TEACHER");
                        }
                    }
                });
        db.collection("STUDENTS")
                .document(number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            navigate("STUDENT");
                        }
                    }
                });
    }

    void navigate(String direction) {
        if (direction.equals("STUDENT")) {
            startActivity(new Intent(this, StudentMainAct.class));
        } else {
            startActivity(new Intent(this, mainDashBoardTeacher.class));
        }
        finish();
    }

    void signUpUser() {
        if (!checkEnDat()) {
            Toast.makeText(this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
            return;
        }
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameSu.getText().toString())
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updateEmail(emailSu.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingPopup.dialogDismiss();
                                startActivity(new Intent(MainActivity.this, SignUpUserActivity.class));
                                finish();
                            }
                        });
                    }
                });
    }

    boolean checkEnDat() {
        String name = nameSu.getText().toString();
        String mail = emailSu.getText().toString();
        String phone = phoneNoSu.getText().toString();
        String state = stateSu.getText().toString();
        return !name.equals("") && !mail.equals("") && !phone.equals("") && !state.equals("");
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            liPb.setVisibility(View.INVISIBLE);
            siWiOt(null, credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            liPb.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Failed TO Login", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            verId = verificationId;
            Toast.makeText(MainActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
        }
    };
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacksSu = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // Custom animation speed or duration.
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    verIcon.setProgress(animation.getAnimatedFraction());
                }
            });
            animator.start();
            verIcon.setRepeatMode(LottieDrawable.RESTART);
            suPb.setVisibility(View.INVISIBLE);
            siWiOtSu(null, credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            suPb.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Failed TO Login", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            verId = verificationId;
            suPb.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
        }
    };
}








