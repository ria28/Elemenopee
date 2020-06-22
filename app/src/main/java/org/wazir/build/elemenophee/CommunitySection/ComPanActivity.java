package org.wazir.build.elemenophee.CommunitySection;

import android.content.Context;
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

import org.wazir.build.elemenophee.CommunitySection.Adapters.AdapterQuestion;
import org.wazir.build.elemenophee.Interfaces.QuesInteract;
import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.ModelObj.LikeObject;
import org.wazir.build.elemenophee.ModelObj.QuesDispObj;
import org.wazir.build.elemenophee.ModelObj.QuestionObj;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;


public class ComPanActivity extends AppCompatActivity implements QuesInteract {
    Spinner claSpiMain, subSpiMain;
    String claSpiMainStr, subSpiMainStr;
    CardView raiseQuestionCard;
    LoadingPopup loading;
    RecyclerView commRcView;
    FirebaseFirestore db;
    ArrayList<QuesDispObj> questions;
    Context context;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_pan);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        context = this;
        initLayout();
        initiateRcView();
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
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        questions.clear();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            QuestionObj obj = snapshot.toObject(QuestionObj.class);
                            QuesDispObj obj1 = new QuesDispObj();

                            obj1.setQuestion(obj.getQuestion());
                            obj1.setDate(obj.getTime());
                            obj1.setAnswersCount(obj.getAnsCount());
                            obj1.setQuesId(obj.getQues_id());
                            obj1.setSolved(obj.isSatisfied());
                            obj1.setStuName(obj.getStuName());
                            obj1.setStuProPic(obj.getStuProfile());
                            obj1.setUpVotes(obj.getLikes());
                            questions.add(obj1);
                        }
                        setupAdapter();
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

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && !task.getResult().exists()) {
                    reference
                            .set(new LikeObject(mAuth.getCurrentUser().getPhoneNumber()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    toQuestions.document(quesId)
                                            .update("likes", FieldValue.increment(1));
                                }
                            });
                } else {
                    Toast.makeText(context, "Already Voted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void giveSolution(String quesId, int pos) {

    }

    @Override
    public void viewQuestion(String quesId, int pos) {

    }

    public void filterQuestions(View view) {
        fetchData(claSpiMainStr, subSpiMainStr);
    }
}