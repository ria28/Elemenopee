package org.wazir.build.elemenophee.Student.StudentSubscription;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.wazir.build.elemenophee.CommunitySection.ComPanActivity;
import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.ModelObj.SubscribedTOmodel;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentSupport.ChatActivity;

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

    TextView title;
    ChipNavigationBar navigationBar;

    boolean fromSearch = false;
    private ArrayList<String> subsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subs);
        fromSearch = getIntent().getBooleanExtra("FROM_SEARCH_STUDENT", false);
        loadingPopup = new LoadingPopup(StudentSubsActivity.this);
        title = findViewById(R.id.textView59);

        reference = FirebaseFirestore.getInstance()
                .collection("TEACHERS");
        subTea = FirebaseFirestore.getInstance()
                .collection("STUDENTS")
                .document(user.getPhoneNumber())
                .collection("SUBSCRIBED_TO");


        searchView = findViewById(R.id.teacherSearch);

        if (!fromSearch) {
            searchView.setVisibility(View.GONE);
            title.setText("Subscriptions");
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
        initActiUi();
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
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            loadingPopup.dismiss();
                            return;
                        }
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

                        }
                        loadingPopup.dismiss();
                    }).addOnFailureListener(e -> {
                Toast.makeText(StudentSubsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingPopup.dialogDismiss();
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void initActiUi() {
        Boolean showOrNot = getIntent().getBooleanExtra("SHOWBN", true);
        navigationBar = findViewById(R.id.chip_nav_bar);
        if (showOrNot) {
            navigationBar.setItemSelected(R.id.id_bn_teacher, true);
            navigationBar.setOnItemSelectedListener(i -> {
                switch (i) {
                    case R.id.id_bn_dashboard:
                        onBackPressed();
                        break;
                    case R.id.id_bn_community:
                        startActivity(new Intent(StudentSubsActivity.this, ComPanActivity.class));
                        finish();
                        break;
                    case R.id.id_bn_chat:
                        startActivity(new Intent(StudentSubsActivity.this, ChatActivity.class));
                        finish();
                        break;
                }
            });
        } else {
            navigationBar.setVisibility(View.GONE);
        }
    }
}