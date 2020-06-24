package org.wazir.build.elemenophee.Student.StuCommPanel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SplashScreen;
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.Chapters;
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.SubComm;
import org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter.ChapterAdapter;
import org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter.SubjectAdapter;
import org.wazir.build.elemenophee.Student.StudentMainPanel.StudentMainAct;
import org.wazir.build.elemenophee.Student.StudentProfile.StudentProfileActivity;
import org.wazir.build.elemenophee.Student.StudentSubscription.StudentSubsActivity;
import org.wazir.build.elemenophee.Student.StudentSupport.ChatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Stu_main_comm_screen extends AppCompatActivity implements SubjectAdapter.OnSubjListener {

    ArrayList<String> Class = new ArrayList<>();
    ArrayList<SubComm> list1 = new ArrayList<>();
    ArrayList<SubComm> list2 = new ArrayList<>();
    ArrayList<Chapters> chapList = new ArrayList<>();
    ArrayAdapter<String> classSpinnerViewAdapter;


    Spinner viewClass;
    Context context;
    String subject;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    RecyclerView first_rv;
    RecyclerView second_rv;
    RecyclerView.Adapter adapter1;
    RecyclerView.Adapter adapter2;

    CollectionReference reference;
    FirebaseAuth mAuth;

    ImageView search;
    EditText searchEdit;
    CircleImageView profilePic, Intro_pic;
    TextView StudentName, name, view_class_tv;
    CardView profileLayout;
    CardView cardLogout;
    CardView Subscribe;
    CardView messages, search_teach;
    CardView search_cv;

    CollectionReference isSubs = FirebaseFirestore.getInstance().collection("/TEACHERS/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main_comm_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.circleImageView);
        Intro_pic = findViewById(R.id.Comm_pro_image);
        StudentName = findViewById(R.id.textView22);
        profileLayout = findViewById(R.id.ProfileTeacher);
        cardLogout = findViewById(R.id.logout);
        Subscribe = findViewById(R.id.stu_subscribe);
        name = findViewById(R.id.textView26);
        messages = findViewById(R.id.message_id);
        view_class_tv = findViewById(R.id.to_view_class);
        search_teach = findViewById(R.id.stu_search_teacher);
        search = findViewById(R.id.searchChap);
        searchEdit = findViewById(R.id.searchEdit);
        search_cv = findViewById(R.id.search_cv);

        getProfilePic();

        search_teach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Stu_main_comm_screen.this, StudentSubsActivity.class);
                intent.putExtra("FROM_SEARCH_STUDENT", true);
                startActivity(intent);
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Stu_main_comm_screen.this, StudentProfileActivity.class);
                startActivity(intent);
            }
        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Stu_main_comm_screen.this, SplashScreen.class));
                finish();
            }
        });

        Subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Stu_main_comm_screen.this, StudentSubsActivity.class);
                startActivity(intent);
            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Stu_main_comm_screen.this, ChatActivity.class);
                startActivity(intent);
                // TODO: 6/21/2020 nav to Messages
            }
        });

        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.requestFocus();
                searchEdit.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEdit, InputMethodManager.SHOW_IMPLICIT);
                search.setVisibility(View.GONE);

//                searchChapters(searchEdit.getText().toString());

            }
        });

        adapter1 = new SubjectAdapter(this, list1, this);
        adapter2 = new ChapterAdapter(this, chapList);

        for (int i = 0; i < 1; i++) {
            System.out.println(i);
        }
//        Log.d("chapters", "onCreate: "+chapList.get(0).getTitle());


        Class.add("Class 5");
        Class.add("Class 6");
        Class.add("Class 7");
        Class.add("Class 8");
        Class.add("Class 9");
        Class.add("Class 10");


        first_rv = findViewById(R.id.first_recycler_view);
        second_rv = findViewById(R.id.second_recycler_view);
        viewClass = findViewById(R.id.viewClassSpinner);

        classSpinnerViewAdapter = new ArrayAdapter<String>(Stu_main_comm_screen.this,
                android.R.layout.simple_spinner_dropdown_item, Class);
        viewClass.setAdapter(classSpinnerViewAdapter);

        first_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        first_rv.hasFixedSize();

        second_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        second_rv.hasFixedSize();

        viewClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view_class_tv.setText(viewClass.getSelectedItem().toString());
//                loadSubject();
                list1.clear();
                list1 = getList2_modified();

                adapter1 = new SubjectAdapter(Stu_main_comm_screen.this, list1, Stu_main_comm_screen.this);
                setUpRecyclerView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        adapter1 = new SubjectAdapter(this, list1, this);
//        adapter2 = new ChapterAdapter(this, chapList);

        setUpRecyclerView();


    }

    private void getProfilePic() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(profilePic.getContext()).load(user.getPhotoUrl()).into(profilePic);
        Glide.with(Intro_pic.getContext()).load(user.getPhotoUrl()).into(Intro_pic);
        StudentName.setText(user.getDisplayName());
        name.setText(user.getDisplayName());
    }

    public void loadSubject() {
        reference = FirebaseFirestore.getInstance()
                .collection("CLASSES")
                .document(viewClass.getSelectedItem().toString())
                .collection("SUBJECT");
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list1.clear();
                            if (!task.getResult().isEmpty()) {

                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    for (int i = 0; i < 1; i++) {
                                        System.out.println(i);
                                    }
                                    subject = doc.getData().toString();
                                    list1 = getList1(list1, subject);
//                                    list1=getList2_modified();
                                    Log.d("subject name", "onComplete: " + subject);

                                }

                                adapter1.notifyDataSetChanged();

                            } else {
                                list1.add(new SubComm("No Subject"));
                                adapter1.notifyDataSetChanged();
                            }
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    ArrayList<SubComm> getList2_modified() {
        Log.d("class", "getList2_modified: " + viewClass.getSelectedItem().toString());
        if (viewClass.getSelectedItem().toString().equals("Class 5")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
        }
        if (viewClass.getSelectedItem().toString().equals("Class 6")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if (viewClass.getSelectedItem().toString().equals("Class 7")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if (viewClass.getSelectedItem().toString().equals("Class 8")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if (viewClass.getSelectedItem().toString().equals("Class 9")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_bio, "Biology"));
            list2.add(new SubComm(R.drawable.ic_physics, "Physics"));
            list2.add(new SubComm(R.drawable.ic_chem, "Chemistry"));
        }
        if (viewClass.getSelectedItem().toString().equals("Class 10")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_bio, "Biology"));
            list2.add(new SubComm(R.drawable.ic_physics, "Physics"));
            list2.add(new SubComm(R.drawable.ic_chem, "Chemistry"));
        }
        return list2;
    }


    ArrayList<SubComm> getList1(ArrayList<SubComm> list1, String sub) {

        switch (sub) {
            case "{Maths=}":
                list1.add(new SubComm(R.drawable.ic_maths, "Maths"));
                break;
            case "{English=}":
                list1.add(new SubComm(R.drawable.ic_english, "English"));
                break;
            case "{Science=}":
                list1.add(new SubComm(R.drawable.ic_sci, "Science"));
                break;
            case "{SST=}":
                list1.add(new SubComm(R.drawable.ic_sst, "S.ST"));
                break;
            case "{EVS=}":
                list1.add(new SubComm(R.drawable.ic_evs, "EVS"));
                break;
            case "{GK=}":
                list1.add(new SubComm(R.drawable.ic_gk, "G.K"));
                break;
            case "{Chemistry=}":
                list1.add(new SubComm(R.drawable.ic_chem, "CHEMISTRY"));
                break;
            case "{Physics=}":
                list1.add(new SubComm(R.drawable.ic_physics, "PHYSICS"));
                break;
            case "{Biology=}":
                list1.add(new SubComm(R.drawable.ic_bio, "BIOLOGY"));
                break;
            case "{History=}":
                list1.add(new SubComm(R.drawable.ic_history, "HISTORY"));
                break;
            case "{Geography=}":
                list1.add(new SubComm(R.drawable.ic_geo, "GEOGRAPHY"));
                break;
            case "{Political Science=}":
                list1.add(new SubComm(R.drawable.ic_polsci, "POL.SCI"));
                break;
            case "{Accounts=}":
                list1.add(new SubComm(R.drawable.ic_accounts, "ACCOUNTS"));
                break;
            case "{Economics=}":
                list1.add(new SubComm(R.drawable.ic_eco, "ECONOMICS"));
                break;
            case "{Business Studies=}":
                list1.add(new SubComm(R.drawable.ic_business, "Business Stu"));
                break;

//                                        default:
//                                            Toast.makeText(context, "Nothing Found", Toast.LENGTH_SHORT).show();
//                                            list1.add(new SubComm("Nothing found"));
//                                            adapter1.notifyDataSetChanged();
//                                            Log.d("size", "onComplete: " + list1.size());
        }
        return list1;
    }

    @Override
    public void onSubjClick(int position) {

        final String SubName = list1.get(position).getSubName();

        if (SubName != null) {
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

                            if (!chapList.isEmpty()) {
                                searchEdit.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        for (int i = 0; i < 1; i++) {
                                            System.out.println(i);
                                        }
                                        Log.d("chapter__", "onTextChanged: " + chapList.get(0).getTitle());
                                        Log.d("text edit", "onTextChanged: " + s);
                                        ChapterAdapter adapter2 = new ChapterAdapter(context, chapList);
                                        if (!chapList.isEmpty()) {
                                            if (adapter2 != null) {
                                                Log.d("Filtered text ", "onTextChanged: " + s);
                                                adapter2.getFilter().filter(s.toString());
                                                adapter2.notifyDataSetChanged();

                                            }
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }

                        }
                    });

        } else
            Toast.makeText(this, "No Subjects", Toast.LENGTH_SHORT).show();


    }

    private void setUpRecyclerView() {
        first_rv.setAdapter(adapter1);
        second_rv.setAdapter(adapter2);
    }
}