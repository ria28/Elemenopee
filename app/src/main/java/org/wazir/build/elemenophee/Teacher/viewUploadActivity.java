package org.wazir.build.elemenophee.Teacher;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;

public class viewUploadActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    notesRecyclerAdapter adapter;
    CollectionReference reference;

    Spinner viewClass,viewSubject,viewChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_upload);

        recyclerView = findViewById(R.id.viewUploadRecycler);
        viewClass = findViewById(R.id.viewUploadClassSpinner);
        viewSubject = findViewById(R.id.viewUploadSubjectSpinner);
        viewChapter = findViewById(R.id.viewUploadChapterSpinner);

        reference = FirebaseFirestore.getInstance().collection("/TEACHERS/8750348232/CLASS/Class 6/SUBJECT/English/CHAPTER/oops/VIDEOS");

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
        CollectionReference SubjectRef = FirebaseFirestore.getInstance().collection("/TEACHERS/8750348232/CLASS");
        CollectionReference ChapterRef = FirebaseFirestore.getInstance().collection("/TEACHERS/8750348232/CLASS");

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}


