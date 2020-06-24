package org.wazir.build.elemenophee.Student.StudentSupport;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
//        mUsers= new ArrayList<>();

        checkStuTea(number);
        setUpRcView();

/*
        if (doc_id != null) {
            for (String s : doc_id) {
                DocumentReference DocRef = reference.document(s).collection("Chats").document("messages");
                DocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

//                        userList.add(doc.get("sendName").toString());
                            String id = doc.get("Phone").toString();
                            String username = doc.get("sendName").toString();
//                            String imageUrl = doc.get("imageUrl").toString();  // not profile image --> send message image
                            User user = new User(id, username, "");
                            mUsers.add(user);

                            userAdapter = new UserAdapter(getContext(), mUsers);
                            userAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(userAdapter);

                        } else
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "No contacts", Toast.LENGTH_SHORT).show();
        }
        mAuth = FirebaseAuth.getInstance();
        number = mAuth.getCurrentUser().getPhoneNumber();
        chatFragmentView = inflater.inflate(R.layout.fragment_chats, container, false);
        IntializeFields();
        getChatrooms();
        RetrieveAndDisplayGroups();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentName = adapterView.getItemAtPosition(position).toString();

                Intent intent = new Intent(getContext(), ChatroomActivity.class);
                intent.putExtra(getString(R.string.intent_chatroom), mChatrooms.get(position));
                startActivity(intent);
            }
        });
        return chatFragmentView;
    }
    private void IntializeFields() {
        list_view = (ListView) chatFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_chats);
        list_view.setAdapter(arrayAdapter);
    }

    private void RetrieveAndDisplayGroups() {

        ChatRef = FirebaseFirestore.getInstance().collection("ChatRoom")
                .document(number).collection("Chats");

        ChatRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        list_of_chats.add(doc.getData().toString());
                    }
                    arrayAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(context, "No chats ", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getChatrooms() {

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);

        CollectionReference chatroomsCollection = mDb
                .collection(getString(R.string.collection_chatrooms));

        mChatroomEventListener = chatroomsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent: called.");

                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Chatroom chatroom = doc.toObject(Chatroom.class);
                        if (!mChatroomIds.contains(chatroom.getChatroom_id())) {
                            mChatroomIds.add(chatroom.getChatroom_id());
                            mChatrooms.add(chatroom);
                        }
                    }
                    Log.d(TAG, "onEvent: number of chatrooms: " + mChatrooms.size());
                    mChatroomRecyclerAdapter.notifyDataSetChanged();
                }

            }
        });
    }
*/
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
        FirebaseFirestore.getInstance().collection("STUDENTS").document(number)
                .collection("Contacts").document("list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            DocumentSnapshot doc = task.getResult();
                            doc_id.add(doc.get("Contacts").toString());
                            Log.d("contacts", "onComplete: " + doc_id.get(0));
                        } else
                            Toast.makeText(ChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        getStudentContacts(doc_id);

                    }
                });

    }

    private void getStudentContacts(ArrayList<String> doc_id) {
        doc_id.add("+918130981088");
        doc_id.add("+918750348232");
//        doc_id.add("+917011974679");

        Log.d("checkk", "getStudentContacts: "+doc_id.get(0));

        if (doc_id != null) {
            for (String s : doc_id) {
               final  String str= s;
                Log.d("string", "getStudentContacts: "+str);
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
//                userAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(userAdapter);
            }


        } else {
            Toast.makeText(ChatActivity.this, "No contacts", Toast.LENGTH_SHORT).show();
        }

    }


    private void Teacher(String number) {
        CollectionReference reference = db.collection("TEACHERS").document(number).collection("Contacts");
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
//                userAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(userAdapter);
            }


        } else {
            Toast.makeText(ChatActivity.this, "No contacts", Toast.LENGTH_SHORT).show();
        }


//        if (doc_id != null) {
//            db.collection("STUDENTS")
//                    .whereIn("contact", Arrays.asList(doc_id))
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                                    DocumentSnapshot doc = snapshot;
//                                    String id = doc.get("contact").toString();
//                                    String username = doc.get("name").toString();
//                                    String imageUrl = doc.get("imageUrl").toString();
//                                    mUsers.add(new User(id, username, imageUrl));
//                                }
//                            } else {
//                                Toast.makeText(ChatActivity.this, "No Students Found", Toast.LENGTH_SHORT).show();
//                            }
//
//                            userAdapter = new UserAdapter(ChatActivity.this, mUsers);
//                            userAdapter.notifyDataSetChanged();
//                            recyclerView.setAdapter(userAdapter);
//                        }
//                    });
////                userAdapter.notifyDataSetChanged();
////                recyclerView.setAdapter(userAdapter);
//        }else
//
//        {
//            Toast.makeText(ChatActivity.this, "No contacts", Toast.LENGTH_SHORT).show();
//        }
    }


    private void setUpRcView() {
        recyclerView.setAdapter(userAdapter);
    }
}


