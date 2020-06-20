package org.wazir.build.elemenophee.Student.StuCommPanel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import org.wazir.build.elemenophee.Student.StudentProfile.StudentProfileActivity;
import org.wazir.build.elemenophee.Student.StudentSubscription.StudentSubsActivity;

import java.util.ArrayList;

public class Stu_main_comm_screen extends AppCompatActivity implements SubjectAdapter.OnSubjListener {

    ArrayList<String> Class = new ArrayList<>();
    ArrayList<SubComm> list1 = new ArrayList<>();
    ArrayList<SubComm> list2 = new ArrayList<>();
    ArrayList<Chapters> chapList = new ArrayList<>();
    ArrayAdapter<String> classSpinnerViewAdapter;


    Spinner viewClass;
    Context context;
    String subject;

    RecyclerView first_rv;
    RecyclerView second_rv;
    RecyclerView.Adapter adapter1;
    RecyclerView.Adapter adapter2;

    CollectionReference reference;
    FirebaseAuth mAuth;

    ImageView profilePic,Intro_pic;
    TextView StudentName,name,view_class_tv;
    LinearLayout profileLayout;
    CardView cardLogout;
    Button Subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main_comm_screen);

        mAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.circleImageView);
        Intro_pic = findViewById(R.id.Comm_pro_image);
        StudentName = findViewById(R.id.textView22);
        profileLayout = findViewById(R.id.ViewProfile);
        cardLogout = findViewById(R.id.logout);
        Subscribe = findViewById(R.id.stu_subscribe);
        name = findViewById(R.id.Comm_name);
        view_class_tv = findViewById(R.id.to_view_class);
        getProfilePic();

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

                Intent intent= new Intent(Stu_main_comm_screen.this, StudentSubsActivity.class);
                startActivity(intent);
            }
        });



        Class.add("Class 6");
        Class.add("Class 7");
        Class.add("Class 8");
        Class.add("Class 9");


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

                view_class_tv.setText( viewClass.getSelectedItem().toString());
//                loadSubject();
                list1.clear();
               list1= getList2_modified();

                adapter1 = new SubjectAdapter(Stu_main_comm_screen.this, list1, Stu_main_comm_screen.this);
                setUpRecyclerView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter1 = new SubjectAdapter(this, list1, this);
        adapter2 = new ChapterAdapter(this, chapList);

        setUpRecyclerView();


    }

    private void getProfilePic() {

        String number = mAuth.getCurrentUser().getPhoneNumber().substring(3);
        FirebaseFirestore.getInstance().collection("STUDENTS").document("+91"+number)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()){
                    StudentObj obj = task.getResult().toObject(StudentObj.class);

                    DocumentSnapshot document = task.getResult();
                    String url= document.getString("imageUrl");
                    Glide.with(profilePic.getContext()).load(url).into(profilePic);
                    Glide.with(Intro_pic.getContext()).load(url).into(Intro_pic);
                    StudentName.setText(obj.getName());
                    String str = "Hi! "+obj.getName();
                    name.setText(str);

                }
            }
        });
    }

    public void loadSubject() {

        reference = FirebaseFirestore.getInstance()
                .collection("CLASSES")
                .document( viewClass.getSelectedItem().toString())
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
                                    Log.d("subject name", "onComplete: "+ subject);

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

    ArrayList<SubComm> getList2_modified(){
        Log.d("class", "getList2_modified: "+ viewClass.getSelectedItem().toString());
        if(viewClass.getSelectedItem().toString().equals("Class 5")){
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
        }
        if(viewClass.getSelectedItem().toString().equals("Class 6")){
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if(viewClass.getSelectedItem().toString().equals("Class 7")){
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if(viewClass.getSelectedItem().toString().equals("Class 8")){
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if(viewClass.getSelectedItem().toString().equals("Class 9")){
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_bio, "Biology"));
            list2.add(new SubComm(R.drawable.ic_physics, "Physics"));
            list2.add(new SubComm(R.drawable.ic_chem, "Chemistry"));
        }
        if(viewClass.getSelectedItem().toString().equals("Class 10")){
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
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        chapList.add(new Chapters(doc.get("CHAPTER").toString()," ", SubName,viewClass.getSelectedItem().toString()));
                                    }
                                    adapter2.notifyDataSetChanged();
                                } else {
                                    chapList.add(new Chapters("No Chapters", SubName));
                                    adapter2.notifyDataSetChanged();
                                }
                            } else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else
            Toast.makeText(this,"No Subjects",Toast.LENGTH_SHORT).show();

    }

    private void setUpRecyclerView() {
        first_rv.setAdapter(adapter1);
        second_rv.setAdapter(adapter2);
    }
}