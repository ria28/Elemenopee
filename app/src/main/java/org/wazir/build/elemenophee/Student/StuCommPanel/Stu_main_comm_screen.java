package org.wazir.build.elemenophee.Student.StuCommPanel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.wazir.build.elemenophee.CommunitySection.ComPanActivity;
import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SplashScreen;
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.Chapters;
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.SubComm;
import org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter.ChapterAdapter;
import org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter.SubjectAdapter;
import org.wazir.build.elemenophee.Student.StudentSubscription.StudentSubsActivity;
import org.wazir.build.elemenophee.Student.StudentSupport.ChatActivity;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.wazir.build.elemenophee.ConstantsRet.getIconsForClass;


public class Stu_main_comm_screen extends AppCompatActivity implements SubjectAdapter.OnSubjListener {

    ArrayList<String> classes = new ArrayList<>();
    ArrayList<SubComm> list1 = new ArrayList<>();
    ArrayList<SubComm> list2 = new ArrayList<>();
    ArrayList<Chapters> chapList = new ArrayList<>();
    ArrayAdapter<String> classSpinnerViewAdapter;


    NiceSpinner viewClass;
    Context context;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    RecyclerView first_rv;
    RecyclerView second_rv;
    RecyclerView.Adapter adapter1;
    RecyclerView.Adapter adapter2;

    CollectionReference reference;
    FirebaseAuth mAuth;

    CircleImageView profilePic, Intro_pic;
    TextView StudentName, name, view_class_tv;
    CardView navToSubs;
    CardView logOutUser;
    CardView navToDashboard;
    CardView navToSettings, navToDownloads;
    FirebaseFirestore db;
    CollectionReference isSubs = FirebaseFirestore.getInstance().collection("/TEACHERS/");

    // activity Specific
    ChipNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main_comm_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initActiUi();
        initUi();
        getProfilePic();
        onClickEvents();
        getClasses();

        setUpRecyclerView();
    }

    private void initActiUi() {
        navigationBar = findViewById(R.id.chip_nav_bar);
        navigationBar.setItemSelected(R.id.id_bn_dashboard, true);
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.id_bn_community:
                        startActivity(new Intent(Stu_main_comm_screen.this, ComPanActivity.class));
                        break;
                    case R.id.id_bn_teacher:
                        Intent intent = new Intent(Stu_main_comm_screen.this, StudentSubsActivity.class);
                        intent.putExtra("FROM_SEARCH_STUDENT", true);
                        startActivity(intent);
                        break;
                    case R.id.id_bn_chat:
                        startActivity(new Intent(Stu_main_comm_screen.this, ChatActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationBar.setItemSelected(R.id.id_bn_dashboard, true);
    }

    private void getProfilePic() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(profilePic.getContext()).load(user.getPhotoUrl()).into(profilePic);
        Glide.with(Intro_pic.getContext()).load(user.getPhotoUrl()).into(Intro_pic);
        StudentName.setText(user.getDisplayName());
        name.setText(user.getDisplayName());
    }

    @Override
    public void onSubjClick(int position) {
        final String SubName = list1.get(position).getSubName();
        if(SubName!=null) {
            reference = FirebaseFirestore.getInstance().collection("CLASSES")
                    .document(viewClass.getSelectedItem().toString())
                    .collection("SUBJECT")
                    .document(SubName).collection("CONTENT");

            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                chapList.clear();
                                if (!task.getResult().isEmpty()) {
                                    for (final QueryDocumentSnapshot doc : task.getResult()) {
                                        isSubs.document(doc.get("TEACHER_ID").toString())
                                                .collection("SUBSCRIBERS")
                                                .whereEqualTo("studentId", user.getPhoneNumber())
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (queryDocumentSnapshots.size() == 1) {
                                                    chapList.add(new Chapters(doc.get("CHAPTER").toString(), doc.get("TEACHER_ID").toString(), SubName, viewClass.getSelectedItem().toString(), true));
                                                } else
                                                    chapList.add(new Chapters(doc.get("CHAPTER").toString(), doc.get("TEACHER_ID").toString(), SubName, viewClass.getSelectedItem().toString(), false));
                                                adapter2.notifyDataSetChanged();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    chapList.add(new Chapters("No Chapters", SubName));
                                    adapter2.notifyDataSetChanged();
                                }
                            } else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else
            Toast.makeText(this, "No Subjects", Toast.LENGTH_SHORT).show();

    }

    private void setUpRecyclerView() {
        first_rv.setAdapter(adapter1);
        second_rv.setAdapter(adapter2);
    }

    private void initUi() {
        mAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.circleImageView);
        Intro_pic = findViewById(R.id.Comm_pro_image);
        StudentName = findViewById(R.id.textView22);
        navToSubs = findViewById(R.id.to_subscriptions);
        logOutUser = findViewById(R.id.to_logout);
        name = findViewById(R.id.textView26);
        navToSettings = findViewById(R.id.to_settings);
        navToDownloads = findViewById(R.id.to_downloads);
        first_rv = findViewById(R.id.first_recycler_view);
        second_rv = findViewById(R.id.second_recycler_view);
        viewClass = findViewById(R.id.viewClassSpinner);
        db = FirebaseFirestore.getInstance();
    }

    private void onClickEvents() {
        navToDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 6/25/2020 navigate to downloads
            }
        });

        navToSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Stu_main_comm_screen.this, StudentSubsActivity.class);
                startActivity(intent);
            }
        });

        logOutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Stu_main_comm_screen.this, SplashScreen.class));
                finish();
            }
        });

        navToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 6/25/2020 nav to Settings
            }
        });
    }

    private void getClasses() {
        db.collection("STUDENTS").document(mAuth.getCurrentUser().getPhoneNumber())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            StudentObj obj = task.getResult().toObject(StudentObj.class);
                            Collections.sort(obj.getClasses());
                            ArrayList<String> classes = new ArrayList<>();
                            for (int i : obj.getClasses()) {
                                classes.add("Class " + i);
                            }

                            spinnerSetup(classes);
                        }
                    }
                });
    }

    private void spinnerSetup(ArrayList<String> standards) {
        classes.addAll(standards);
        // initial Classes setup
        list1 = getIconsForClass(standards.get(0));
        adapter1 = new SubjectAdapter(Stu_main_comm_screen.this, list1, Stu_main_comm_screen.this);
        setUpRecyclerView();

        classSpinnerViewAdapter = new ArrayAdapter<String>(
                Stu_main_comm_screen.this,
                android.R.layout.simple_spinner_dropdown_item,
                classes
        );
        viewClass.setAdapter(classSpinnerViewAdapter);

        first_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        first_rv.hasFixedSize();

        second_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        second_rv.hasFixedSize();
        viewClass.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                list1 = getIconsForClass(parent.getItemAtPosition(position).toString());
                adapter1 = new SubjectAdapter(Stu_main_comm_screen.this, list1, Stu_main_comm_screen.this);
                setUpRecyclerView();
            }
        });
        adapter1 = new SubjectAdapter(this, list1, this);
        adapter2 = new ChapterAdapter(this, chapList);

    }
}