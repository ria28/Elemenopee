package org.wazir.build.elemenophee.Student.Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Teacher.viewUploadActivity;

import java.util.ArrayList;
import java.util.Map;

public class ViewNotesActivity extends AppCompatActivity {

    String title;
    String SubName;
    String classs;
    RecyclerView recyclerView;
    ArrayList<contentModel> pdfList = new ArrayList<>();
    notesRecyclerAdapter notesAdapter;

    private CollectionReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        recyclerView = findViewById(R.id.viewNotesRecycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        notesAdapter = new notesRecyclerAdapter(this, pdfList);

        Intent intent = getIntent();
        title = intent.getStringExtra("CHAPTER_TITLE");
        SubName = intent.getStringExtra("SUBJECT");
        classs = intent.getStringExtra("CLASS");

        loadNotes();
        setUpRecyclerView();


    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(notesAdapter);
    }

    private void loadNotes() {

        Log.d("Chap title", "loadNotes: " + title);
//        for (int i = 0; i < 1; i++) {
//            System.out.println(i);
//        }

        reference = FirebaseFirestore.getInstance().collection("/CLASSES/" + classs +
                "/SUBJECT/" + SubName + "/CONTENT/"
        );
        reference.whereEqualTo("CHAPTER", title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            pdfList.clear();
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.get("NOTES") != null) {
                                        for (Map<String, Object> obj : (ArrayList<Map<String, Object>>) doc.getData().get("NOTES")) {
                                            pdfList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                    , (Timestamp) obj.get("timeStamp")));
                                        }
                                        notesAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "No Data found Related to this Chapter", Toast.LENGTH_SHORT).show();
                                notesAdapter.notifyDataSetChanged();

                            }
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
