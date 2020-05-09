package org.wazir.build.elemenophee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText registerEmail, registerPass;
    Button registerBtn;
    RadioGroup registerCategory;

    private String Email, Pass, category;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        registerCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb != null && checkedId > -1) {
                    category = rb.getText() + "";
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Email = registerEmail.getText() + "";
                Pass = registerPass.getText() + "";

                if (Email != null && !Email.isEmpty() && Pass != null && !Pass.isEmpty() && category != null && !category.isEmpty())
                    mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage() + "", Toast.LENGTH_LONG).show();
                        }
                    });
                else
                    Toast.makeText(getApplicationContext(), "please fill", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void init() {
        registerEmail = findViewById(R.id.registerEmail);
        registerPass = findViewById(R.id.registerPass);
        registerBtn = findViewById(R.id.registerBtn);
        registerCategory = findViewById(R.id.registerCategory);

        registerCategory.clearCheck();
    }
}
