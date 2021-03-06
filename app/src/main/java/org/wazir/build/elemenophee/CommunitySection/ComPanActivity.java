package org.wazir.build.elemenophee.CommunitySection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.wazir.build.elemenophee.CommunitySection.Adapters.AdapterQuestion;
import org.wazir.build.elemenophee.Interfaces.QuesInteract;
import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.ModelObj.LikeObject;
import org.wazir.build.elemenophee.ModelObj.QuesDispObj;
import org.wazir.build.elemenophee.ModelObj.QuestionObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentSubscription.StudentSubsActivity;
import org.wazir.build.elemenophee.Student.StudentSupport.ChatActivity;

import java.util.ArrayList;


public class ComPanActivity extends AppCompatActivity implements QuesInteract {
    //JAVA objs
    String claSpiMainStr, subSpiMainStr;
    LoadingPopup loading;
    ArrayList<QuesDispObj> questions;

    // Android widgets
    Spinner claSpiMain, subSpiMain;
    CardView raiseQuestionCard;
    RecyclerView commRcView;
    Context context;
    ChipNavigationBar navigationBar;

    // Firebase Stuff
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_pan);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        context = this;
        initLayout();
        initiateRcView();
        initActiUi();
    }

    void initLayout() {
        claSpiMain = findViewById(R.id.spinner4);
        subSpiMain = findViewById(R.id.spinner3);
        claSpiMainStr = "5";
        subSpiMainStr = "English";
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        loading = new LoadingPopup(this);
        questions = new ArrayList<>();

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this, R.array.classes_array, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        claSpiMain.setAdapter(classAdapter);

        claSpiMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                claSpiMainStr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> subAdapterMain = ArrayAdapter.createFromResource(this, R.array.subs_array, android.R.layout.simple_spinner_item);
        subAdapterMain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpiMain.setAdapter(subAdapterMain);

        subSpiMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subSpiMainStr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        raiseQuestionCard = findViewById(R.id.id_raise_ques);
    }

    void initiateRcView() {
        commRcView = findViewById(R.id.community_main_screen_ID);
        commRcView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        fetchData(claSpiMainStr, subSpiMainStr);
    }

    void fetchData(String cla, String sub) {
        CollectionReference reference = db.collection("QUESTIONS").document(cla).collection(sub);
        reference
                .orderBy("likes", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots != null) {
                        questions.clear();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            QuestionObj obj = snapshot.toObject(QuestionObj.class);
                            QuesDispObj obj1 = new QuesDispObj();

                            obj1.setQuestion(obj.getQuestion());
                            obj1.setDate(obj.getTime());
                            obj1.setAnswersCount(obj.getAnsCount());
                            obj1.setQuesId(obj.getQues_id());
                            obj1.setAnswersCount(obj.getAnsCount());
                            obj1.setStuName(obj.getStuName());
                            obj1.setStuProPic(obj.getStuProfile());
                            obj1.setUpVotes(obj.getLikes());
                            questions.add(obj1);
                        }
                        setupAdapter();
                    } else {
                        Toast.makeText(context, "No Querys", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void setupAdapter() {
        AdapterQuestion adapter = new AdapterQuestion(questions, this);
        adapter.setInteract(this);
        commRcView.setAdapter(adapter);
    }

    public void askQues(View view) {
        loading.askQuestionPopup();
    }

    @Override
    public void voteQuestion(final String quesId, int pos) {
        final CollectionReference toQuestions = db.collection("QUESTIONS").document(claSpiMainStr).collection(subSpiMainStr);
        final DocumentReference reference = toQuestions.document(quesId).collection("LIKES").document(mAuth.getCurrentUser().getPhoneNumber());

        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().exists()) {
                reference
                        .set(new LikeObject(mAuth.getCurrentUser().getPhoneNumber()))
                        .addOnCompleteListener(task1 -> toQuestions.document(quesId).update("likes", FieldValue.increment(1)));
            } else {
                Toast.makeText(context, "Already Voted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void giveSolution(String quesId, int pos) {
        loading.postSolution(quesId, claSpiMainStr, subSpiMainStr);
    }

    @Override
    public void viewQuestion(String quesId, int pos) {
        Intent intent = new Intent(ComPanActivity.this, SolutionDisplayActivity.class);
        intent.putExtra("QUESTION_ID", quesId);
        intent.putExtra("QUESTION_CLASS", claSpiMainStr);
        intent.putExtra("QUESTION_SUB", subSpiMainStr);
        startActivity(intent);
    }

    public void filterQuestions(View view) {
        fetchData(claSpiMainStr, subSpiMainStr);
    }

    private void initActiUi() {
        navigationBar = findViewById(R.id.chip_nav_bar);
        Boolean bool = getIntent().getBooleanExtra("SHOWBN", true);
        if (bool) {
            navigationBar.setItemSelected(R.id.id_bn_community, true);
            navigationBar.setOnItemSelectedListener(i -> {
                switch (i) {
                    case R.id.id_bn_dashboard:
                        onBackPressed();
                        break;
                    case R.id.id_bn_teacher:
                        Intent intent = new Intent(ComPanActivity.this, StudentSubsActivity.class);
                        intent.putExtra("FROM_SEARCH_STUDENT", true);
                        startActivity(intent);
                        break;
                    case R.id.id_bn_chat:
                        startActivity(new Intent(ComPanActivity.this, ChatActivity.class));
                        finish();
                        break;
                }
            });
        } else {
            navigationBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationBar.setItemSelected(R.id.id_bn_community, true);
    }
}