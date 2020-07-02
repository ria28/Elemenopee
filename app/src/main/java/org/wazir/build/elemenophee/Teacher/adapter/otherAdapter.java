package org.wazir.build.elemenophee.Teacher.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Utils.downloadAndStoreNotes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class otherAdapter extends RecyclerView.Adapter<otherAdapter.ViewHolder> {


    Context context;
    ArrayList<contentModel> otherList;
    CollectionReference contentref = FirebaseFirestore.getInstance().collection("/CLASSES/");
    CollectionReference recentRef = FirebaseFirestore.getInstance().collection("/TEACHERS/");
    AlertDialog.Builder builder;
    AlertDialog alert;
    StorageReference storageReference;
    boolean fromViewUploads = false;

    public otherAdapter(Context context, ArrayList<contentModel> otherList,boolean fromViewUploads) {
        this.context = context;
        this.otherList = otherList;
        builder = new AlertDialog.Builder(context);
        this.fromViewUploads = fromViewUploads;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upload_recycler_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy \nHH:mm");

        if (otherList.size() > 0) {
            holder.title.setText(otherList.get(position).getFileTitle());
            holder.timeStamp.setText(sfd.format(otherList.get(position).getTimeStamp().toDate()));
            holder.image.setImageResource(R.drawable.classroom);
            holder.options.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, holder.options);
                popupMenu.inflate(R.menu.video_option_menu);
                if(!fromViewUploads){
                    popupMenu.getMenu().findItem(R.id.deleteOption).setVisible(false);
                }
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.downloadOption:
                            String mime = otherList.get(position).getMime();
                            File notesDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Elemenophee/Videos");
                            File outputFile = new File(notesDirectory, otherList.get(position).getFileTitle() + "." + mime.substring(mime.lastIndexOf("/") + 1));
                            if (!notesDirectory.exists()) {
                                notesDirectory.mkdirs();
                            }

                            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibrator.vibrate(25);
                            }

                            downloadAndStoreNotes downloadAndStore = downloadAndStoreNotes.getInstance(context);

                            downloadAndStore.openFILE(outputFile, otherList.get(position).getFileUrl()
                                    , otherList.get(position).getFileTitle(), otherList.get(position).getMime(), "Videos");
                            break;
                        case R.id.deleteOption:
                            builder.setMessage("Do you want to delete ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", (dialog, id) -> {
                                        deleteContent(position);
                                    })
                                    .setNegativeButton("No", (dialog, id) -> {

                                    });
                            //Creating dialog box
                            alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Delete");
                            alert.show();
                        default:
                            return false;
                    }
                    return true;
                });
                popupMenu.show();
            });

            holder.itemView.setOnClickListener(v -> {
                String mime = otherList.get(position).getMime();

                File notesDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Elemenophee/Notes");
                File outputFile = new File(notesDirectory, otherList.get(position).getFileTitle() + "." + mime.substring(mime.lastIndexOf("/") + 1));
                Toast.makeText(context, MimeTypeMap.getFileExtensionFromUrl(otherList.get(position).getFileUrl()) + "", Toast.LENGTH_SHORT).show();
                if (!notesDirectory.exists()) {
                    notesDirectory.mkdirs();
                }

                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(25);
                }

                downloadAndStoreNotes downloadAndStore = downloadAndStoreNotes.getInstance(context);

                downloadAndStore.openFILE(outputFile, otherList.get(position).getFileUrl()
                        , otherList.get(position).getFileTitle(), otherList.get(position).getMime(), "Other");
            });
        }
    }

    private void deleteContent(int position) {

        CollectionReference delRef = contentref.document(otherList.get(position).getClas())
                .collection("/SUBJECT/")
                .document(otherList.get(position).getSubject())
                .collection("/CONTENT/");

        delRef
                .whereEqualTo("CHAPTER", otherList.get(position).getChapter())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        delRef.document(doc.getId()).update("OTHER", FieldValue.arrayRemove(otherList.get(position)));
                        recentRef.document(otherList.get(position).getTeacherID())
                                .collection("RECENT_UPLOADS")
                                .document("UPLOADS")
                                .collection("OTHER")
                                .document(otherList.get(position).getFileTitle())
                                .delete()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(otherList.get(position).getFileUrl());
                                        storageReference
                                                .delete()
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                }).addOnFailureListener(exception -> {
                                            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                    } else {
                                        Toast.makeText(context, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public int getItemCount() {
        return otherList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timeStamp;
        public ImageView image, options;


        public ViewHolder(View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.viewUploaditemTimeStamp);
            title = itemView.findViewById(R.id.viewUploaditemTitle);
            image = itemView.findViewById(R.id.viewUploaditemImage);
            options = itemView.findViewById(R.id.imageView10);
        }

    }

}
