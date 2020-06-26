package org.wazir.build.elemenophee.Student.StudentSupport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.wazir.build.elemenophee.CommunitySection.ComPanActivity;
import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.ModelObj.TempObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentSubscription.StudentSubsActivity;
import org.wazir.build.elemenophee.Student.StudentSupport.Chat121.User;
import org.wazir.build.elemenophee.Student.StudentSupport.Chat121.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> mUsers = new ArrayList<>();
    FirebaseUser fuser;
    FirebaseFirestore db;
    CollectionReference reference;
    ArrayList<String> doc_id;

    FirebaseAuth mAuth;
    ChipNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        doc_id = new ArrayList<>();
        userAdapter = new UserAdapter(this, mUsers);
        recyclerView = findViewById(R.id.chats_list_rv_);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = db.collection("ChatRoom");
        mAuth = FirebaseAuth.getInstance();
        String number = mAuth.getCurrentUser().getPhoneNumber();
        doc_id = new ArrayList<>();

        checkStuTea(number);
        setUpRcView();
        initActiUi();
    }

    private void checkStuTea(final String number) {

        if (mAuth.getCurrentUser() != null) {
            db.collection("STUDENTS").document(number).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().exists()) {
                                Student(number);
                            }
                        }
                    });

            db.collection("TEACHERS").document(number).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().exists()) {
                                Teacher(number);
                            }
                        }
                    });
        }

    }

    private void Student(String number) {
        FirebaseFirestore.getInstance()
                .collection("STUDENTS")
                .document(number)
                .collection("Contacts")
                .document("list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            TempObj doc = task.getResult().toObject(TempObj.class);
                            doc_id.addAll(doc.getContacts());
                        }
                        getStudentContacts(doc_id);
                    }
                });

    }

    private void getStudentContacts(ArrayList<String> doc_id) {
        if (doc_id != null) {
            FirebaseFirestore.getInstance().collection("TEACHERS")
                    .whereIn("phone", doc_id)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                TeacherObj tObj = snapshot.toObject(TeacherObj.class);
                                String id = tObj.getPhone();
                                String username = tObj.getName();
                                String imageUrl = "";
                                if (tObj.getProPicURL() == null) {
                                    imageUrl = "";
                                } else
                                    imageUrl = tObj.getProPicURL();
                                mUsers.add(new User(id, username, imageUrl));
                            }
                            userAdapter = new UserAdapter(ChatActivity.this, mUsers);
                            userAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(userAdapter);
                        }
                    });
        } else {
            Toast.makeText(ChatActivity.this, "No contacts", Toast.LENGTH_SHORT).show();
        }

    }

    private void Teacher(String number) {
        FirebaseFirestore.getInstance()
                .collection("TEACHERS")
                .document(number)
                .collection("Contacts")
                .document("list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            TempObj doc = task.getResult().toObject(TempObj.class);
                            doc_id.addAll(doc.getContacts());
                        }
                        getTeacherContacts(doc_id);
                    }
                });
    }

    private void getTeacherContacts(ArrayList<String> doc_id) {
        if (doc_id != null) {
            for (String s : doc_id) {
                final String str = s;
                FirebaseFirestore.getInstance()
                        .collection("STUDENTS")
                        .document(s)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    DocumentSnapshot doc = task.getResult();
                                    StudentObj obj1 = task.getResult().toObject(StudentObj.class);
                                    String id = str;
                                    String username = doc.get("name").toString();
                                    String imageUrl;

                                    for (int i = 0; i < 1; i++) {
                                        System.out.println(i);
                                    }

                                    if (!obj1.getmImageUrl().equals("")) {
                                        imageUrl = obj1.getmImageUrl();
                                    } else
                                        imageUrl = "";

                                    mUsers.add(new User(id, username, imageUrl));
                                    userAdapter = new UserAdapter(ChatActivity.this, mUsers);
                                    userAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(userAdapter);

                                } else
                                    Toast.makeText(ChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        });
            }
        } else {
            Toast.makeText(ChatActivity.this, "No contacts", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRcView() {
        recyclerView.setAdapter(userAdapter);
    }

    private void initActiUi() {
        navigationBar = findViewById(R.id.chip_nav_bar);
        navigationBar.setItemSelected(R.id.id_bn_chat, true);
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.id_bn_dashboard:
                        onBackPressed();
                        break;
                    case R.id.id_bn_community:
                        startActivity(new Intent(ChatActivity.this, ComPanActivity.class));
                        finish();
                        break;
                    case R.id.id_bn_teacher:
                        Intent intent = new Intent(ChatActivity.this, StudentSubsActivity.class);
                        intent.putExtra("FROM_SEARCH_STUDENT", true);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
    }
}


