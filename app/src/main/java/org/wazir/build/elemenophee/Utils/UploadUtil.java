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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.notesModel;

public class UploadUtil extends Worker {

    private StorageReference ref;
    Uri selectedFilePath;
    int uploadProgress = 0;
    String[] fileData;
    CollectionReference collectionReference;


    public UploadUtil(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Data data = getInputData();
//        UploadModel model = (UploadModel) data.getClass(UploadActivity.UPLOAD_UTIL);
        selectedFilePath = Uri.parse(data.getString("fileURI"));
        fileData = data.getStringArray("FILE_INFO");
        upload();
        return Result.success();
    }

    private void upload() {

        collectionReference = FirebaseFirestore.getInstance()
                .collection("TEACHERS/8750348232/CLASS/"+
                        fileData[0]+ "/"+ "SUBJECT/" +
                        fileData[1]+ "/"+ "CHAPTER/" +
                        fileData[2]+ "/"+ "VIDEOS"
                );


        ref = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference();

        final StorageReference reference = ref.child("VIDEOS/"+fileData[3]);

        displayNotification(fileData[3],"0%");

        reference.putFile(selectedFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        collectionReference.document(fileData[3])
                                .set(new notesModel(fileData[3], uri + ""))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
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
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }



        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
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
                    builder.setProgress(100, uploadProgress, false);
                    manager.notify(1, builder.build());
                    SystemClock.sleep(500);
                }
                builder.setContentText("Uploaded")
                        .setProgress(0, 0, false)
                        .setOngoing(false);
                manager.notify(1, builder.build());
            }
        }).start();
    }
}
