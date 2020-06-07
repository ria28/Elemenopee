package org.wazir.build.elemenophee.Student.Community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.Community.CommAdapter.ChapterAdapter;
import org.wazir.build.elemenophee.Student.Community.CommAdapter.MainAdapterComm;
import org.wazir.build.elemenophee.Student.Community.CommAdapter.SubjectAdapter;
import org.wazir.build.elemenophee.Student.StudentActivity.ClassRoomActivity;
import org.wazir.build.elemenophee.Teacher.model.contentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

public class MainCommScreen extends AppCompatActivity implements SubjectAdapter.OnSubjListener {

    static int count = 0;
    SubComm subject;
    RecyclerView recyclerView;
    private static ArrayList<Object> objects = new ArrayList<>();
    static ArrayList<String> Chapter;
    static ArrayList<SubComm> list1;
    ChapterAdapter chapterAdapter = new ChapterAdapter();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference subjRef = db.collection("CLASSES").document("Class 6").collection("SUBJECT");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comm_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        recyclerView = findViewById(R.id.recycler_View_comm);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainCommScreen.this));
        recyclerView.setAdapter(new MainAdapterComm(MainCommScreen.this, getObject(), this));
    }

    private  ArrayList<Object> getObject() {

        objects.add(getSubjects().get(0));
        if (getChapters().size() != 0)
            objects.add(getChapters().get(0));
        chapterAdapter.notifyDataSetChanged();


        return objects;
    }

    @NonNull
    public static ArrayList<SubComm> getSubjects() {
        list1 = new ArrayList<>();

        list1.add(new SubComm(R.drawable.maths, "Maths"));
        list1.add(new SubComm(R.drawable.sci, "Science"));
        list1.add(new SubComm(R.drawable.sst, "Sst"));
//        list1.add(new SubComm(R.drawable.maths));
//        list1.add(new SubComm(R.drawable.sci));
//        list1.add(new SubComm(R.drawable.sst));

        return list1;
    }


    @Override
    public void onSubjClick(int position) {
        subject = list1.get(position);
        String SubName = subject.getSubName();


        Chapter = new ArrayList<>();

        subjRef.document("English").collection("CONTENT")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Chapter.clear();
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Chapter.add(doc.get("CHAPTER").toString());
                                    chapterAdapter.notifyDataSetChanged();
                                    count++;
                                    Log.d("size", "onComplete "+ count);
                                }
                            } else {
                                Chapter.add("No Chapters");
                                chapterAdapter.notifyDataSetChanged();

                            }
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @NonNull
    public static ArrayList<Chapters> getChapters() {
        ArrayList<Chapters> list2 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list2.add(new Chapters(Chapter.get(i)));
        }
        return list2;

//        list2.add(new Chapters("1. Laws Of Motion ", "A Brief Description of the Chapter over here\n" +
//                "which will give a glimpse of the chapter to the\n" +
//                "Reader and make it look long "));

    }


}