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
import com.google.firebase.storage.StorageReference;

import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.model.contentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UploadUtil extends Worker {

    private StorageReference ref;
    Uri selectedFilePath;
    int uploadProgress = 0;
    String[] fileData;
    CollectionReference collectionReference;
    CollectionReference recentRef;
    CollectionReference TeacherRef;
    String userPhone;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    int videoCount = 0 ;


    public UploadUtil(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Data data = getInputData();
        selectedFilePath = Uri.parse(data.getString("fileURI"));
        userPhone = data.getString("USER_PHONE");
        fileData = data.getStringArray("FILE_INFO");

        if (!data.getBoolean("ADD_TO_EXISTING", false))
            uploadToNewChapter();
        else
            uploadToExistingChapter();

        return Result.success();
    }

    private void addToRECENTS(contentModel model) {
        TeacherRef = FirebaseFirestore.getInstance()
                .collection("/TEACHERS/");
        recentRef = FirebaseFirestore.getInstance()
                .collection("/TEACHERS/" +
                        user.getPhoneNumber() + "/RECENT_UPLOADS/" +
                        "UPLOADS/" +
                        fileData[4]
                );
        recentRef.document(model.getFileTitle())
                .set(model).addOnSuccessListener(aVoid -> {
            TeacherRef
                    .document(Objects.requireNonNull(user.getPhoneNumber()))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            TeacherObj obj = task.getResult().toObject(TeacherObj.class);
                            videoCount = obj.getVideoCount();
                            TeacherRef
                                    .document(Objects.requireNonNull(user.getPhoneNumber()))
                                    .update("videoCount", videoCount + 1)
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage() + "", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    });
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void uploadToExistingChapter() {

        collectionReference = FirebaseFirestore.getInstance()
                .collection("/CLASSES/" +
                        fileData[0] + "/SUBJECT/" +
                        fileData[1] + "/" +
                        "CONTENT"
                );


        ref = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference();

        final StorageReference reference = ref.child(fileData[4] + "/" + fileData[3]);

        displayNotification(fileData[3], "0%");

        reference.putFile(selectedFilePath).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                reference.getMetadata().addOnSuccessListener(storageMetadata -> collectionReference.document(fileData[5])
                        .update(fileData[4], FieldValue.arrayUnion(new contentModel(fileData[3],
                                uri.toString(), Timestamp.now(), fileData[6], user.getPhoneNumber(), storageMetadata.getContentType())))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                addToRECENTS(new contentModel(fileData[3],
                                        uri.toString(), Timestamp.now(), fileData[6], user.getPhoneNumber()
                                        , storageMetadata.getContentType()));
                            } else
                                Toast.makeText(getApplicationContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                        }));

            }
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show())).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            uploadProgress = (int) progress;
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

        final StorageReference reference = ref.child(fileData[4] + "/" + fileData[3]);

        displayNotification(fileData[3], "0%");

        reference.putFile(selectedFilePath).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                reference.getMetadata().addOnSuccessListener(storageMetadata -> {
                    ArrayList<contentModel> nM = new ArrayList<>();
                    nM.add(new contentModel(fileData[3],
                            uri.toString(), Timestamp.now(), fileData[6], user.getPhoneNumber(), storageMetadata.getContentType()));
                    Map<String, Object> chapter = new HashMap<>();
                    chapter.put("CHAPTER", fileData[2]);
                    chapter.put("TEACHER_ID", userPhone);
                    chapter.put(fileData[4], nM);

                    collectionReference.document(userPhone + Timestamp.now().toDate())
                            .set(chapter)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    addToRECENTS(new contentModel(fileData[3],
                                            uri.toString(), Timestamp.now(), fileData[6], user.getPhoneNumber(), storageMetadata.getContentType()));
                                } else
                                    Toast.makeText(getApplicationContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                            });
                });
            }
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show())).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            uploadProgress = (int) progress;
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
