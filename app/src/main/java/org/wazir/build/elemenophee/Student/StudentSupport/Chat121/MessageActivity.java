package org.wazir.build.elemenophee.Student.StudentSupport.Chat121;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentSupport.ChatGroup.GroupChatActivity;
import org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel.MessObj;
import org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel.MessageAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

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
    private StorageTask mUploadTask;
    FirebaseAuth mAuth;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_image = findViewById(R.id.profile_image_message);
        username = findViewById(R.id.username_message);
        btn_send = findViewById(R.id.send);
        text_send = findViewById(R.id.text_send);
        tempImage = findViewById(R.id.sendImage);

        recyclerView = findViewById(R.id.message_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        userId = intent.getStringExtra("user_id");    /// other person id

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference();

//        reference=database.getInstace.ref(child).child(userid)


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = text_send.getText().toString();
                if (!message.equals("")) {
                    sendMessage(fuser.getUid(), userId, message, fuser.getDisplayName());
                } else
                    Toast.makeText(MessageActivity.this, "YOU CAN'T SEND EMPTY MESSAGE", Toast.LENGTH_SHORT).show();
                text_send.setText("");

            }
        });
        readMessage(fuser.getUid(), userId, "");

    }


    private void sendMessage(String sender, String receiver, String msg, String name) {

        if (tempImage.getVisibility() == View.VISIBLE) {
            uploadFile();
            tempImage.setVisibility(View.INVISIBLE);
            tempImage.setImageBitmap(null);
            text_send.setText("");
        } else {

            MessObj obj = new MessObj(msg, sender, receiver, name);
            mchats = messageAdapter.getMessages();
            mchats.add(0, obj);
            messageAdapter.setMessages(mchats);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String doc_id = sdf.format(new Date());
            saveDocId = doc_id;

            db.collection("ChatRoom").document(doc_id).collection("Chats")
                    .document("messages").set(obj);

            db.collection("STUDENTS").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).set(doc_id);
        }

    }


    private void readMessage(final String myid, String userid, String imageUrl) {
        CollectionReference reference = db.collection("ChatRoom").document(saveDocId).collection("Chats");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mchats = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    mchats.add(snapshot.toObject(MessObj.class));
                }
//                Collections.reverse(mchats);
                messageAdapter.setMessages(mchats);
            }
        });
    }

    public void getMedia_(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {
            final StorageReference fileReference = mStorageRef.child("images/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            tempImage.setVisibility(View.INVISIBLE);
                            Toast.makeText(MessageActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    MessObj object = new MessObj(text_send.getText().toString(),
                                            mAuth.getCurrentUser().getUid(),
                                            userId,
                                            mAuth.getCurrentUser().getDisplayName());
                                    object.setImageUrl(uri.toString());
                                    mchats = messageAdapter.getMessages();
                                    mchats.add(0, object);
                                    messageAdapter.setMessages(mchats);

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String doc_id = sdf.format(new Date());
                                    db.collection("ChatRoom").document(saveDocId).collection("Chats").document("messages")
                                            .set(object)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        imageUri = null;
                                                        Toast.makeText(MessageActivity.this, "Message Posted", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MessageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
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

}