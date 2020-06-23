package org.wazir.build.elemenophee.CommunitySection;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.CommunitySection.Adapters.AdapterSolutions;
import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.ModelObj.AnsObj;
import org.wazir.build.elemenophee.ModelObj.QuestionObj;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SolutionDisplayActivity extends AppCompatActivity {
    String quesId, clas, sub;
    ArrayList<AnsObj> solutions;

    // layout Elements
    CircleImageView quesUserPic;
    TextView quesUserName, ques, quesTime;
    RecyclerView solRcview;
    Context mCtx;

    //class initializer
    LoadingPopup loader;
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_display);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getIntentData();
        initLayout();

    }

    void getIntentData() {
        quesId = getIntent().getStringExtra("QUESTION_ID");
        clas = getIntent().getStringExtra("QUESTION_CLASS");
        sub = getIntent().getStringExtra("QUESTION_SUB");
    }

    void initLayout() {
        quesUserName = findViewById(R.id.textView50);
        ques = findViewById(R.id.textView51);
        quesUserPic = findViewById(R.id.circleImageView4);
        quesTime = findViewById(R.id.textView55);
        solutions = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mCtx = this;
        setupRcView();
    }

    void fetchData() {
        DocumentReference docRef = db.collection("QUESTIONS").document(clas).collection(sub).document(quesId);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            QuestionObj quesDispObj = task.getResult().toObject(QuestionObj.class);
                            quesUserName.setText(quesDispObj.getStuName());
                            quesTime.setText(quesDispObj.getTime());
                            Glide.with(mCtx).load(quesDispObj.getStuProfile()).into(quesUserPic);
                            ques.setText(quesDispObj.getQuestion());
                        }
                    }
                });
        docRef.collection("SOLUTIONS")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        solutions.clear();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            AnsObj obj = snapshot.toObject(AnsObj.class);
                            solutions.add(obj);
                        }
                        solRcview.setAdapter(new AdapterSolutions(solutions, getApplicationContext()));
                    }
                });

    }

    void setupRcView() {
        solRcview = findViewById(R.id.sol_rcview_id);
        solRcview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        fetchData();
    }

    public void postSol(View view) {
        loader = new LoadingPopup(this);
        loader.postSolution(quesId, clas, sub);
    }
}