package org.wazir.build.elemenophee.Student.StudentSupport;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentSupport.Chat121.User;
import org.wazir.build.elemenophee.Student.StudentSupport.Chat121.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> mUsers = new ArrayList<>();
    List<String> userList;
    FirebaseUser fuser;
    FirebaseFirestore db;
    CollectionReference reference;
    ArrayList<String> doc_id;

    FirebaseAuth mAuth;
    TeacherObj teachers;
    Toolbar mToolbar;
    Integer person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        doc_id = new ArrayList<>();
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Student Support");


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
        FirebaseFirestore.getInstance().collection("STUDENTS")
                .document(number)
                .collection("Contacts")
                .document("list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            DocumentSnapshot doc = task.getResult();
                            ArrayList<String> res = (ArrayList<String>) doc.get("Contacts");
                            doc_id.addAll(res);
                        } else {
                            Toast.makeText(ChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        getStudentContacts(doc_id);

                    }
                });
    }

    private void getStudentContacts(ArrayList<String> doc_id) {
        if (doc_id != null) {
            for (String s : doc_id) {
               final  String str= s;
                FirebaseFirestore.getInstance().collection("TEACHERS").document(s)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    DocumentSnapshot doc = task.getResult();
//                                    String id = doc.get("phone").toString();
                                    String id = str;
                                    String username = doc.get("name").toString();

                                    String imageUrl = doc.get("proPicURL").toString();

                                    mUsers.add(new User(id, username, imageUrl));
                                    userAdapter = new UserAdapter(ChatActivity.this, mUsers);
                                    userAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(userAdapter);

                                }
                            }

                        });
            }
        } else {
            Toast.makeText(ChatActivity.this, "No contacts", Toast.LENGTH_SHORT).show();
        }

    }


    private void Teacher(String number) {
        CollectionReference reference = db.collection("ChatRoom").document(number).collection("Chats");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        doc_id.add(snapshot.getId());
                    }
                } else {
                    Toast.makeText(ChatActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                getTeacherContacts(doc_id);
            }
        });
    }

    private void getTeacherContacts(ArrayList<String> doc_id) {
        if (doc_id != null) {
            for (String s : doc_id) {
                final String str = s;
                FirebaseFirestore.getInstance().collection("STUDENTS").document(s)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    DocumentSnapshot doc = task.getResult();
                                    String id = str;
                                    String username = doc.get("name").toString();
                                    String imageUrl = doc.get("imageUrl").toString();

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
}


