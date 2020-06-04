package org.wazir.build.elemenophee.Teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;

import java.util.ArrayList;


public class viewUploadActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    notesRecyclerAdapter adapter;
    CollectionReference reference;

    Spinner viewClass,viewSubject,viewChapter;
    ArrayList<String> Class = new ArrayList<>();
    ArrayList<String> Subject = new ArrayList<>();
    ArrayList<String> Chapter = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_upload);

        recyclerView = findViewById(R.id.viewUploadRecycler);
        viewClass = findViewById(R.id.viewUploadClassSpinner);
        viewSubject = findViewById(R.id.viewUploadSubjectSpinner);
        viewChapter = findViewById(R.id.viewUploadChapterSpinner);

        Class.add("SELECT");
        Subject.add("SELECT");
        Chapter.add("SELECT");

        ArrayAdapter<String> classSpinnerViewAdapter = new ArrayAdapter<String>(viewUploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, Class);
        ArrayAdapter<String> subjectSpinnerViewAdapter = new ArrayAdapter<String>(viewUploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, Subject);
        ArrayAdapter<String> chapterSpinnerViewAdapter = new ArrayAdapter<String>(viewUploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, Chapter);

        reference = FirebaseFirestore.getInstance().collection("/TEACHERS/8750348232/CLASS/Class 6/SUBJECT/English/CHAPTER/oops/VIDEOS");

        viewClass.setAdapter(classSpinnerViewAdapter);
        viewSubject.setAdapter(subjectSpinnerViewAdapter);
        viewChapter.setAdapter(chapterSpinnerViewAdapter);


//        viewClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CollectionReference subjectRef = FirebaseFirestore.getInstance()
//                        .collection("/TEACHERS/8750348232/CLASS/"
//                        + viewClass.getSelectedItem()+"/SUBJECT");
//                subjectRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        Subject.clear();
//                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
//                            Subject.add(doc.getId());
//                        }
//                    }
//                });
//            }
//        });

        Query query = reference;
        setUpRecyclerView(query);


    }

    private void setUpRecyclerView(Query q) {

        FirestoreRecyclerOptions<notesModel> options = new FirestoreRecyclerOptions.Builder<notesModel>()
                .setQuery(q, notesModel.class).build();

        adapter = new notesRecyclerAdapter(options, viewUploadActivity.this,reference,false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    void getUploadInfo(){
        CollectionReference classRef = FirebaseFirestore.getInstance().collection("/TEACHERS/8750348232/CLASS");
        classRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    Class.add(doc.getId());
                }
            }
        });
        CollectionReference SubjectRef = FirebaseFirestore.getInstance().collection("/TEACHERS/8750348232/CLASS");
        CollectionReference ChapterRef = FirebaseFirestore.getInstance().collection("/TEACHERS/8750348232/CLASS");

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        getUploadInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}


