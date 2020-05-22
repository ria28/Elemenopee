package org.wazir.build.elemenophee.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.wazir.build.elemenophee.MainActivity;
import org.wazir.build.elemenophee.R;

public class UploadUtil extends Worker {

    private StorageReference ref;
    Uri selectedFilePath;
    int uploadProgress = 0;
    private UploadTask uploadTask;


    public UploadUtil(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Data data = getInputData();
//        UploadModel model = (UploadModel) data.getClass(UploadActivity.UPLOAD_UTIL);
        selectedFilePath = Uri.parse(data.getString("fileURI"));
        upload();
        return Result.success();
    }

    private void upload() {

        ref = FirebaseStorage.getInstance("gs://elemenophee-a0ac5.appspot.com/").getReference();

        StorageReference reference = ref.child("random");

        displayNotification("task", "taskdes");

        uploadTask = reference.putFile(selectedFilePath);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
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
