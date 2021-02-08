package org.wazir.build.elemenophee.Student.StudentSupport.Chat121;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel.MessObj;
import org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel.MessageAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    boolean userIsTeacher;
    CircleImageView profile_image;
    TextView username;
    FirebaseUser fuser;
    Intent intent;

    ImageView btn_send;
    EditText text_send;

    FirebaseFirestore db;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    ArrayList<MessObj> mchats;
    String saveDocId;
    ImageView tempImage;
    private StorageReference mStorageRef;
    Uri imageUri;
    FirebaseAuth mAuth;
    String userId;
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        checkCategory();
        mchats = new ArrayList<>();
        profile_image = findViewById(R.id.profile_image_message);
        username = findViewById(R.id.username_message);
        btn_send = findViewById(R.id.send);
        text_send = findViewById(R.id.text_send);
//        scrollView=findViewById(R.id.scroll);

        recyclerView = findViewById(R.id.message_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setReverseLayout(true);

        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mchats = new ArrayList<>();
        messageAdapter = new MessageAdapter(mchats, this);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.scrollToPosition(mchats.size()-1);


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        userId = getIntent().getStringExtra("user_id");

        if (userId != null) {
            db.collection("STUDENTS").document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().exists()) {
                                setStudent(userId);
                            }
                        }
                    });

            db.collection("TEACHERS").document(userId).get()                        // student's account chat with teacher
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().exists()) {
                                setTeacher(userId);
                            }
                        }
                    });
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String doc_id = sdf.format(new Date());
        saveDocId = doc_id;

        btn_send.setOnClickListener(v -> {
            String message = text_send.getText().toString();
            sendMessage(fuser.getPhoneNumber(), userId, message, fuser.getDisplayName());
        });

        readMessage(fuser.getPhoneNumber(), userId);
    }

    private void setStudent(String userId) {
        db.collection("STUDENTS")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            username.setText(doc.get("name").toString());
                            String imageUrl;
                            if (doc.get("mImageUrl") != null) {
                                imageUrl = doc.get("mImageUrl").toString();
                            } else {
                                imageUrl = "";
                            }
                            Glide.with(profile_image.getContext()).load(imageUrl).into(profile_image);
                        }

                    }
                });
    }

    private void setTeacher(String userId) {
        db.collection("TEACHERS")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        DocumentSnapshot doc = task.getResult();
                        username.setText(doc.get("name").toString());
                        if(doc.get("proPicURL")==null) {
                            Glide.with(profile_image.getContext()).load(R.drawable.ic_person).into(profile_image);
                        }
                        else {
                            String imageUrl = doc.get("proPicURL").toString();
                            Glide.with(profile_image.getContext()).load(imageUrl).into(profile_image);
                        }

                    }

                });

    }

    private void sendMessage(final String sender, final String receiver, String msg, String name) {

        if (!msg.equals("")) {
            final MessObj obj = new MessObj(msg, sender, receiver, name);
            mchats = messageAdapter.getMessages();
            mchats.add(0, obj);
            messageAdapter.setMessages(mchats);
            messageAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(messageAdapter);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String doc_id = sdf.format(new Date());
            saveDocId = doc_id;
            if (receiver != null) {
                db.collection("STUDENTS")
                        .document(receiver)
                        .get()                        // teacher's account chat with students
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult().exists()) {

                                    db.collection("ChatRoom")
                                            .document(sender)
                                            .collection("Chats")
                                            .document(receiver)
                                            .collection("Chats")
                                            .document(saveDocId).set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            finish();
                                        }
                                    });

                                    db.collection("STUDENTS")
                                            .document(receiver).collection("Contacts")
                                            .document("list").update("contacts", FieldValue.arrayUnion(userId));
                                    db.collection("TEACHERS")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("Contacts")
                                            .document("list").update("contacts", FieldValue.arrayUnion(receiver));
                                }
                            }
                        });

                db.collection("TEACHERS").document(receiver).get()                        // student's account chat with teacher
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult().exists()) {

                                    db.collection("ChatRoom").document(receiver).collection("Chats").document(sender).collection("Chats")
                                            .document(saveDocId).set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            finish();
                                        }
                                    });
                                    db.collection("STUDENTS").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                                            .collection("Contacts").document("list").update("contacts", FieldValue.arrayUnion(receiver));

                                    db.collection("TEACHERS").document(receiver).collection("Contacts")
                                            .document("list").update("contacts", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()));

                                }
                            }
                        });
            }
        } else
            Toast.makeText(MessageActivity.this, "YOU CAN'T SEND EMPTY MESSAGE", Toast.LENGTH_SHORT).show();

        text_send.setText("");
    }


    private void readMessage(final String myid, final String userid) {
        if (userid != null) {

            db.collection("STUDENTS").document(userid).get()                         // as a teacher chat with student
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().exists()) {

                                CollectionReference reference = db.collection("ChatRoom").document(myid).collection("Chats")
                                        .document(userid).collection("Chats");

                                reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        mchats = new ArrayList<>();
                                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            mchats.add(snapshot.toObject(MessObj.class));
                                        }
//                                        Collections.reverse(mchats);
                                        messageAdapter.setMessages(mchats);
                                        recyclerView.scrollToPosition(mchats.size()-1);

                                    }
                                });

                            }
                        }
                    });

            db.collection("TEACHERS").document(userid).get()                           // as a student  chat with teacher
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().exists()) {
                                CollectionReference reference = db.collection("ChatRoom").document(userid)
                                        .collection("Chats").document(myid)
                                        .collection("Chats");
                                reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        mchats = new ArrayList<>();
                                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            mchats.add(snapshot.toObject(MessObj.class));
                                        }
//                                        Collections.reverse(mchats);
                                        messageAdapter.setMessages(mchats);
                                        recyclerView.scrollToPosition(mchats.size()-1);

                                    }
                                });

                            }
                            Collections.reverse(mchats);
                            messageAdapter.setMessages(mchats);
                        }
                });
            }
        }


    public void getMedia_(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(tempImage);
            tempImage.setVisibility(View.VISIBLE);
        }
    }

    void checkCategory() {
        FirebaseFirestore.getInstance().collection("TEACHER")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            userIsTeacher = true;
                        } else {
                            userIsTeacher = false;
                        }
                    }
                });
    }

}