package org.wazir.build.elemenophee.Student.StuCommPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.Chapters;
import org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter.ChapterAdapter;
import org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter.SubjectAdapter;
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.SubComm;
import org.wazir.build.elemenophee.Student.StudentMainPanel.StudentMainAct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Stu_main_comm_screen extends AppCompatActivity implements SubjectAdapter.OnSubjListener {

    ArrayList<String> Class = new ArrayList<>();
    ArrayList<SubComm> list1 = new ArrayList<>();
    ArrayList<Chapters> chapList = new ArrayList<>();
    ArrayAdapter<String> classSpinnerViewAdapter;


    Spinner viewClass;

//    StudentMainAct obj = new StudentMainAct();

    Context context;
    String subject;

    RecyclerView first_rv;
    RecyclerView second_rv;
    RecyclerView.Adapter adapter1;
    RecyclerView.Adapter adapter2;

    CollectionReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main_comm_screen);

//        Class = obj.Class;
//        Log.d("Class size", "onCreate: "+ Class.size());

//        Class.add(6);
//        Class.add(7);

//        Class.add("Class 6");
        Class.add("Class 7");
        Class.add("Class 8");

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

                loadSubject();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter1 = new SubjectAdapter(this, list1, this);
        adapter2 = new ChapterAdapter(this, chapList);

        setUpRecyclerView();


    }

    public void loadSubject() {

        reference = FirebaseFirestore.getInstance()
                .collection("CLASSES")
                .document( viewClass.getSelectedItem().toString())
                .collection("SUBJECT");
//        for (int i = 0; i < 1; i++) {
//            System.out.println(i);
//        }
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
                list1.add(new SubComm(R.drawable.ic_phy, "PHYSICS"));
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
            reference = FirebaseFirestore.getInstance().collection("CLASSES").document(viewClass.getSelectedItem().toString()).collection("SUBJECT")
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