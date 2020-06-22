package org.wazir.build.elemenophee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.ModelObj.QuestionObj;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadingPopup extends AppCompatDialog {

    Context     ctx;
    AlertDialog alertDialog;
    QuestionObj questionObj;
    String      subject, stuClass;
    ProgressBar quesPb;

    public LoadingPopup(Context context) {
        super(context);
        ctx = context;
    }

    public void dialogRaise() {
        if (ctx == null) {
            return;
        }
        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        final View view1 = LayoutInflater.from(ctx).inflate(R.layout.layout_alert_progress, null);
        alert.setView(view1);
        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void dialogDismiss() {
        if (ctx == null) {
            return;
        }
        alertDialog.dismiss();
    }

    public void askQuestionPopup() {
        if (ctx == null) {
            return;
        }
        questionObj = new QuestionObj();
        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        final View view1 = LayoutInflater.from(ctx).inflate(R.layout.ask_question_layout, null);
        alert.setView(view1);
        final TextInputLayout question = view1.findViewById(R.id.textInputLayout10);
        Spinner classSpinner = view1.findViewById(R.id.spinner);
        Spinner subSpinner = view1.findViewById(R.id.spinner2);
        quesPb = view1.findViewById(R.id.progressBar5);

        ArrayAdapter<CharSequence> subAdapter = ArrayAdapter.createFromResource(ctx, R.array.subs_array, android.R.layout.simple_spinner_item);
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpinner.setAdapter(subAdapter);

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(ctx, R.array.classes_array, android.R.layout.simple_spinner_item);
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);

        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stuClass = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view1.findViewById(R.id.cardView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!question.getEditText().getText().equals("")) {
                    questionObj.setQuestion(question.getEditText().getText().toString());
                    questionObj.setStuId(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
                    questionObj.setTime(sdf.format(new Date()));
                    questionObj.setSubClass(stuClass);
                    questionObj.setSubject(subject);
                    beginAskQues(questionObj);
                }
            }
        });
        view1.findViewById(R.id.cardView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void beginAskQues(QuestionObj obj) {
        quesPb.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("QUESTIONS")
                .document(stuClass)
                .collection(subject)
                .document()
                .set(obj)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            alertDialog.dismiss();
                        } else {
                            quesPb.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void pushAnswerPopup(){

    }
}
