package org.wazir.build.elemenophee.Student.StudentSupport.ChatGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
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
import org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel.MessObj;
import org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel.MessageAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    RecyclerView chats;
    ArrayList<MessObj> messages;
    TextInputLayout message;
    MessageAdapter adapter;
    FirebaseAuth mAuth;
    String questionId;
    FirebaseFirestore db;
    ImageView tempImage;
    private StorageReference mStorageRef;
    Uri imageUri;
    private StorageTask mUploadTask;
    private ScrollView mScrollView;
    String groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        chats = findViewById(R.id.chat_screen);
        tempImage = findViewById(R.id.imageView18);
        message = findViewById(R.id.message_id_send);
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
//        questionId = getIntent().getStringExtra("ROOM_ID");
        questionId="";
        groupName = getIntent().getStringExtra("groupName");
        messages = new ArrayList<>();
        getMessages();
        adapter = new MessageAdapter(messages, this);
        mAuth = FirebaseAuth.getInstance();
        chats.setAdapter(adapter);
        chats.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
    }

    void getMessages() {
//        CollectionReference reference = db.collection(questionId);
//        String number = mAuth.getCurrentUser().getPhoneNumber();
        CollectionReference reference = db.collection("ChatRoom").document(groupName)
                .collection("Groups");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MessObj> obj = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    obj.add(snapshot.toObject(MessObj.class));
                }
                Collections.reverse(obj);
                adapter.setMessages(obj);
            }
        });

        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void sendMessage(View view) {
        if (tempImage.getVisibility() == View.VISIBLE) {
            uploadFile();
            tempImage.setVisibility(View.INVISIBLE);
            tempImage.setImageBitmap(null);
            message.getEditText().setText("");
        } else {
            SharedPreferences preferences = getSharedPreferences("USER", MODE_PRIVATE);
            int stu = preferences.getInt("TYPE", 0);
            MessObj object;
            if (stu == 0) {
                object = new MessObj(message.getEditText().getText().toString(),
                        mAuth.getCurrentUser().getUid(),
                        questionId,
                        preferences.getString("NAME", "NO_NAME"));
            } else {
                object = new MessObj(message.getEditText().getText().toString(),
                        mAuth.getCurrentUser().getUid(),
                        questionId,
                        mAuth.getCurrentUser().getDisplayName());
            }

            String number = mAuth.getCurrentUser().getPhoneNumber();
            messages = adapter.getMessages();
            messages.add(0, object);
            adapter.setMessages(messages);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String doc_id = sdf.format(new Date());
            db.collection("ChatRoom").document(groupName).collection("Groups").document("messages")
                    .set(object)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GroupChatActivity.this, "Message Posted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            message.getEditText().setText("");
        }
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }


    public void getMedia(View view) {
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
                            Toast.makeText(GroupChatActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    MessObj object = new MessObj(message.getEditText().getText().toString(),
                                            mAuth.getCurrentUser().getUid(),
                                            groupName,
                                            mAuth.getCurrentUser().getDisplayName());
                                    object.setImageUrl(uri.toString());
                                    messages = adapter.getMessages();
                                    messages.add(0, object);
                                    adapter.setMessages(messages);

                                    String number = mAuth.getCurrentUser().getPhoneNumber();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String doc_id = sdf.format(new Date());
                                    db.collection("ChatRoom").document(groupName).collection("Groups").document("messages")
                                            .set(object)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        imageUri = null;
                                                        Toast.makeText(GroupChatActivity.this, "Message Posted", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GroupChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
