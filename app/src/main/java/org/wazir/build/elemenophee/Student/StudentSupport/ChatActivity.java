package org.wazir.build.elemenophee.Student.StudentSupport;

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

import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentSupport.Chat121.User;
import org.wazir.build.elemenophee.Student.StudentSupport.Chat121.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> mUsers=new ArrayList<>();
    List<String> userList;
    FirebaseUser fuser;
    FirebaseFirestore db;
    CollectionReference reference;
    ArrayList<String> doc_id =new ArrayList<>();
    FirebaseAuth mAuth;
    TeacherObj teachers;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Student Support");



        userAdapter=new UserAdapter(this,mUsers);
        recyclerView = findViewById(R.id.chats_list_rv_);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userAdapter);


        db = FirebaseFirestore.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = db.collection("ChatRoom");
        mAuth = FirebaseAuth.getInstance();
        String number = mAuth.getCurrentUser().getPhoneNumber();

        //get all contacts

        // Student login
        FirebaseFirestore.getInstance().collection("STUDENTS").document(number).collection("Contacts").document("list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            DocumentSnapshot doc = task.getResult();
                            doc_id.add(doc.get("Contacts").toString());
                            Log.d("contacts", "onComplete: "+ doc_id.get(0));
                        } else
                            Toast.makeText(ChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

//        Log.d("Contacts", "onCreate: "+doc_id.get(0));

        // Teacher login

//        FirebaseFirestore.getInstance().collection("TEACHERS").document(number).collection("Contacts").document("list")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful() && task.getResult().exists()) {
//                            DocumentSnapshot doc = task.getResult();
//                            doc_id.add(doc.get("Contacts").toString());
//                        } else
//                            Toast.makeText(ChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });


        //doc_id teacher's number
        doc_id.add("+918130981088");
        doc_id.add("+918750348232");

        if (doc_id != null) {
            for (String s : doc_id) {
                FirebaseFirestore.getInstance().collection("TEACHERS").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            DocumentSnapshot doc = task.getResult();
                            String id = doc.get("phone").toString();
                            String username = doc.get("name").toString();
                            String imageUrl = doc.get("proPicURL").toString();

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

        setUpRcView();

//        if (doc_id != null) {
//            for (String s : doc_id) {
//                DocumentReference DocRef = reference.document(s).collection("Chats").document("messages");
//                DocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot doc = task.getResult();
//
////                        userList.add(doc.get("sendName").toString());
//                            String id = doc.get("Phone").toString();
//                            String username = doc.get("sendName").toString();
////                            String imageUrl = doc.get("imageUrl").toString();  // not profile image --> send message image
//                            User user = new User(id, username, "");
//                            mUsers.add(user);
//
//                            userAdapter = new UserAdapter(getContext(), mUsers);
//                            userAdapter.notifyDataSetChanged();
//                            recyclerView.setAdapter(userAdapter);
//
//                        } else
//                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } else {
//            Toast.makeText(getContext(), "No contacts", Toast.LENGTH_SHORT).show();
//        }
//        mAuth = FirebaseAuth.getInstance();
//        number = mAuth.getCurrentUser().getPhoneNumber();
//        chatFragmentView = inflater.inflate(R.layout.fragment_chats, container, false);


//        IntializeFields();
//        getChatrooms();
//        RetrieveAndDisplayGroups();

//
//        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                String currentName = adapterView.getItemAtPosition(position).toString();
//
//                Intent intent = new Intent(getContext(), ChatroomActivity.class);
//                intent.putExtra(getString(R.string.intent_chatroom), mChatrooms.get(position));
//                startActivity(intent);
//            }
//        });


//        return chatFragmentView;
//    }


//    private void IntializeFields() {
//        list_view = (ListView) chatFragmentView.findViewById(R.id.list_view);
//        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_chats);
//        list_view.setAdapter(arrayAdapter);
//    }
//
//    private void RetrieveAndDisplayGroups() {
//
//        ChatRef = FirebaseFirestore.getInstance().collection("ChatRoom")
//                .document(number).collection("Chats");
//
//        ChatRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        list_of_chats.add(doc.getData().toString());
//                    }
//                    arrayAdapter.notifyDataSetChanged();
//                } else
//                    Toast.makeText(context, "No chats ", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }
//
//    private void getChatrooms() {
//
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setTimestampsInSnapshotsEnabled(true)
//                .build();
//        mDb.setFirestoreSettings(settings);
//
//        CollectionReference chatroomsCollection = mDb
//                .collection(getString(R.string.collection_chatrooms));
//
//        mChatroomEventListener = chatroomsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                Log.d(TAG, "onEvent: called.");
//
//                if (e != null) {
//                    Log.e(TAG, "onEvent: Listen failed.", e);
//                    return;
//                }
//
//                if (queryDocumentSnapshots != null) {
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//
//                        Chatroom chatroom = doc.toObject(Chatroom.class);
//                        if (!mChatroomIds.contains(chatroom.getChatroom_id())) {
//                            mChatroomIds.add(chatroom.getChatroom_id());
//                            mChatrooms.add(chatroom);
//                        }
//                    }
//                    Log.d(TAG, "onEvent: number of chatrooms: " + mChatrooms.size());
//                    mChatroomRecyclerAdapter.notifyDataSetChanged();
//                }
//
//            }
//        });
//    }
    }

    private void setUpRcView() {
        recyclerView.setAdapter(userAdapter);
    }
}
