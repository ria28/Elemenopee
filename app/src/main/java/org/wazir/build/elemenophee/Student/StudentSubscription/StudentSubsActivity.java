package org.wazir.build.elemenophee.Student.StudentSubscription;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.ModelObj.SubscribedTOmodel;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class StudentSubsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    CollectionReference reference;
    LoadingPopup loadingPopup;
    Query query;
    CollectionReference subTea;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    StuTeacherAdapter adapter;
    StuTeacherAdapter searchAdapter;

    boolean fromSearch = false;
    private ArrayList<String> subsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subs);
        fromSearch = getIntent().getBooleanExtra("FROM_SEARCH_STUDENT", false);
        loadingPopup = new LoadingPopup(StudentSubsActivity.this);

        reference = FirebaseFirestore.getInstance()
                .collection("TEACHERS");
        subTea = FirebaseFirestore.getInstance().collection("STUDENTS")
                .document(user.getPhoneNumber()).collection("SUBSCRIBED_TO");


        searchView = findViewById(R.id.teacherSearch);

        if(!fromSearch){
            searchView.setVisibility(View.GONE);
        }
        recyclerView = findViewById(R.id.teachers_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()){
                    changeQuery(query);
                    Log.d("TAG", "onQueryTextSubmit: "+query);
                    return true;
                }else{
                    searchAdapter.stopListening();
                    recyclerView.setAdapter(adapter);
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    searchAdapter.stopListening();
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });

        getTeacherList();

    }

    private void changeQuery(String q) {
        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        query = reference.whereEqualTo("name",q);
        FirestorePagingOptions<TeacherObj> pagingOptions = new FirestorePagingOptions.Builder<TeacherObj>()
                .setQuery(query, config, TeacherObj.class)
                .build();

        searchAdapter = new StuTeacherAdapter(pagingOptions, StudentSubsActivity.this);
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
    }

    private void getTeacherList() {
        loadingPopup.dialogRaise();

        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        if (fromSearch) {
            query = reference;

            FirestorePagingOptions<TeacherObj> pagingOptions = new FirestorePagingOptions.Builder<TeacherObj>()
                    .setQuery(query, config, TeacherObj.class)
                    .build();

            adapter = new StuTeacherAdapter(pagingOptions, StudentSubsActivity.this);
            adapter.startListening();
            recyclerView.setAdapter(adapter);
            loadingPopup.dialogDismiss();

        } else {
            subTea
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                SubscribedTOmodel model = doc.toObject(SubscribedTOmodel.class);
                                subsList.add(model.getTeacherID());
                            }
                            if (subsList.size() > 0) {
                                query = reference.whereIn("phone", subsList);
                                FirestorePagingOptions<TeacherObj> pagingOptions = new FirestorePagingOptions.Builder<TeacherObj>()
                                        .setQuery(query, config, TeacherObj.class)
                                        .build();

                                adapter = new StuTeacherAdapter(pagingOptions, StudentSubsActivity.this);
                                adapter.startListening();
                                recyclerView.setAdapter(adapter);
                                loadingPopup.dialogDismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StudentSubsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingPopup.dialogDismiss();
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}