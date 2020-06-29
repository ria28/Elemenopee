package org.wazir.build.elemenophee;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.Student.StuCommPanel.Stu_main_comm_screen;
import org.wazir.build.elemenophee.Teacher.mainDashBoardTeacher;

import java.util.Objects;
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

        loginUser.setOnClickListener(v -> {
            if (otpLi.getEditText().getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "Where is the OTP ??????", Toast.LENGTH_SHORT).show();
                return;
            }
            loadingPopup.dialogRaise();
            siWiOt(otpLi.getEditText().getText().toString(), null);
        });
        signUpUser.setOnClickListener(v -> {
            signUpUser();
            loadingPopup.dialogRaise();
        });
        verifyBtnSu.setOnClickListener(v -> {
            if (otpSu.getText().toString().equals("")) {
                Toast.makeText(this, "Enter a valid OTP", Toast.LENGTH_SHORT).show();
            } else {
                if (otpSu.getText().toString().length() == 6)
                    siWiOtSu(otpSu.getText().toString(), null);
                else
                    Toast.makeText(this, "Enter A Valid OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TextWatcher phNuChangeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String tempNumber = s.toString();
            if (tempNumber.length() > 10) {
                if (tempNumber.substring(0, 3).equals("+91") && tempNumber.length() == 13) {
                    checkUserPresent(s.toString());
                    liPb.setVisibility(View.VISIBLE);
                }
            } else if (tempNumber.length() == 10 && !tempNumber.substring(0, 3).equals("+91")) {
                checkUserPresent("+91" + s.toString());
                liPb.setVisibility(View.VISIBLE);
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
            String tempNumber = s.toString();
            if (tempNumber.length() > 10) {
                if (tempNumber.substring(0, 3).equals("+91") && tempNumber.length() == 13) {
                    sendOtpSu(tempNumber);
                    suPb.setVisibility(View.VISIBLE);
                }
            } else if (tempNumber.length() == 10 && !tempNumber.substring(0, 3).equals("+91")) {
                sendOtpSu("+91" + tempNumber);
                suPb.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    void sendOtp(String phoneNumber) {
        if (phoneNumber.length() > 10) {
            number = phoneNumber;
        } else {
            number = "+91" + phoneNumber;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    void sendOtpSu(String phoneNumber) {
        if (phoneNumber.length() > 10) {
            number = phoneNumber;
        } else {
            number = "+91" + phoneNumber;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacksSu
        );
    }

    void siWiOt(String otp, PhoneAuthCredential cred) {
        PhoneAuthCredential credential;
        if (cred == null && otp != null) {
            credential = PhoneAuthProvider.getCredential(verId, otp);
        } else {
            credential = cred;
        }
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            stuOrTea();
                        } else {
                            liPb.setVisibility(View.INVISIBLE);
                            loadingPopup.dialogDismiss();
                        }
                    }
                });
    }

    void siWiOtSu(String otp, PhoneAuthCredential cred) {
        PhoneAuthCredential credential;
        if (cred == null && otp != null) {
            credential = PhoneAuthProvider.getCredential(verId, otp);
        } else {
            credential = cred;
        }
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
        db.collection("STUDENTS")
                .document(number)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        navigate("STUDENT");
                    } else
                        db.collection("TEACHERS")
                                .document(number)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult().exists()) {
                                        navigate("TEACHER");
                                    }
                                    loadingPopup.dialogDismiss();
                                });
                });
    }

    void navigate(String direction) {
        if (direction.equals("STUDENT")) {
            startActivity(new Intent(this, Stu_main_comm_screen.class));
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
                .addOnCompleteListener(task -> user.updateEmail(emailSu.getText().toString()).addOnCompleteListener(task1 -> {
                    loadingPopup.dialogDismiss();
                    Intent intent = new Intent(MainActivity.this, SignUpUserActivity.class);
                    intent.putExtra("MAIL", emailSu.getText().toString());
                    startActivity(intent);
                    finish();
                }));
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
            loadingPopup.dialogDismiss();
            Toast.makeText(MainActivity.this, "Login Success ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            liPb.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Failed TO Login"+ e.getMessage(), Toast.LENGTH_SHORT).show();

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
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    verIcon.setProgress(animation.getAnimatedFraction());
                    Toast.makeText(MainActivity.this, "User Verified", Toast.LENGTH_SHORT).show();
                }
            });
            animator.start();
            verIcon.setRepeatMode(LottieDrawable.RESTART);
            suPb.setVisibility(View.INVISIBLE);
//            loadingPopup.dialogDismiss();
            siWiOtSu(null, credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            suPb.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Failed TO SignUp " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            verId = verificationId;
            suPb.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
        }
    };

    void checkUserPresent(final String userId) {
        db.collection("STUDENTS").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && Objects.requireNonNull(task.getResult()).exists()) {
                        sendOtp(userId);
                    } else {
                        db.collection("TEACHERS")
                                .document(userId)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && Objects.requireNonNull(task1.getResult()).exists()) {
                                        sendOtp(userId);
                                    } else {
                                        Toast.makeText(MainActivity.this, "User Don't Exists", Toast.LENGTH_SHORT).show();
                                        liPb.setVisibility(View.INVISIBLE);
                                    }
                                });
                    }
                });
    }
}








