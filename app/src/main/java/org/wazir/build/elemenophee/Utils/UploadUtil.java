package org.wazir.build.elemenophee.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.model.contentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadUtil extends Worker {

    private StorageReference ref;
    Uri selectedFilePath;
    int uploadProgress = 0;
    String[] fileData;
    CollectionReference collectionReference;
    CollectionReference recentRef;
    String userEmail;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public UploadUtil(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Data data = getInputData();
        selectedFilePath = Uri.parse(data.getString("fileURI"));
        userEmail = data.getString("USER_EMAIL");
        fileData = data.getStringArray("FILE_INFO");

        if (!data.getBoolean("ADD_TO_EXISTING", false))
            uploadToNewChapter();
        else
            uploadToExistingChapter();

        return Result.success();
    }

    private void addToRECENTS(contentModel model) {
        recentRef = FirebaseFirestore.getInstance()
                .collection("/TEACHERS/" +
                        user.getPhoneNumber() + "/RECENT_UPLOADS/" +
                        "UPLOADS/" +
                        fileData[4]
                );
        recentRef.document(model.getFileTitle())
                .set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadToExistingChapter() {

        collectionReference = FirebaseFirestore.getInstance()
                .collection("/CLASSES/" +
                        fileData[0] + "/SUBJECT/" +
                        fileData[1] + "/" +
                        "CONTENT"
                );


        ref = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference();

        final StorageReference reference = ref.child(fileData[4]+"/" + fileData[3]);

        displayNotification(fileData[3], "0%");

        reference.putFile(selectedFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {

                        collectionReference.document(fileData[5])
                                .update(fileData[4], FieldValue.arrayUnion(new contentModel(fileData[3], uri.toString(), Timestamp.now())))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            addToRECENTS(new contentModel(fileData[3], uri.toString(), Timestamp.now()));
                                        } else
                                            Toast.makeText(getApplicationContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                uploadProgress = (int) progress;
            }
        });

    }

    private void uploadToNewChapter() {

        collectionReference = FirebaseFirestore.getInstance()
                .collection("/CLASSES/" +
                        fileData[0] + "/SUBJECT/" +
                        fileData[1] + "/" +
                        "CONTENT"
                );


        ref = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference();

        final StorageReference reference = ref.child(fileData[4]+"/" + fileData[3]);

        displayNotification(fileData[3],"0%");

        reference.putFile(selectedFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {

                        ArrayList<contentModel> nM = new ArrayList<>();
                        nM.add(new contentModel(fileData[3], uri.toString(), Timestamp.now()));
                        Map<String, Object> chapter = new HashMap<>();
                        chapter.put("CHAPTER", fileData[2]);
                        chapter.put("TEACHER_ID",userEmail);
                        chapter.put(fileData[4], nM);

                        collectionReference.document(userEmail + Timestamp.now().toDate())
                                .set(chapter)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            addToRECENTS(new contentModel(fileData[3], uri.toString(), Timestamp.now()));
                                        } else
                                            Toast.makeText(getApplicationContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                uploadProgress = (int) progress;
            }
        });


    }

    private void displayNotification(String task, String desc) {

        final NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("elemenophee", "elemenopheeUPLOAD", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }



        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "elemenophee")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(100, 0, false)
                .setOngoing(true)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        manager.notify(1, builder.build());

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                while (uploadProgress != 100) {
                    builder.setProgress(100, uploadProgress, false)
                    .setContentText(uploadProgress + "%    uploaded..");
                    manager.notify(1, builder.build());
                    SystemClock.sleep(200);
                }
                builder.setContentText("Uploaded")
                        .setProgress(0, 0, false)
                        .setOngoing(false);
                manager.notify(1, builder.build());
            }
        }).start();
    }
}
