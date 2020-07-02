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


public class notesRecyclerAdapter extends RecyclerView.Adapter<notesRecyclerAdapter.ViewHolder> {


    Context context;
    ArrayList<contentModel> pdfList;
    CollectionReference contentref = FirebaseFirestore.getInstance().collection("/CLASSES/");
    CollectionReference recentRef = FirebaseFirestore.getInstance().collection("/TEACHERS/");
    AlertDialog.Builder builder;
    AlertDialog alert;
    StorageReference storageReference;
    boolean fromViewUploads = false;


    public notesRecyclerAdapter(Context context, ArrayList<contentModel> pdfList,boolean fromViewUploads) {
        this.context = context;
        this.pdfList = pdfList;
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

        if (pdfList.size() > 0) {
            holder.title.setText(pdfList.get(position).getFileTitle());
            holder.timeStamp.setText(sfd.format(pdfList.get(position).getTimeStamp().toDate()));
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
                            String mime = pdfList.get(position).getMime();
                            File notesDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Elemenophee/Videos");
                            File outputFile = new File(notesDirectory, pdfList.get(position).getFileTitle() + "." + mime.substring(mime.lastIndexOf("/") + 1));
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

                            downloadAndStore.openFILE(outputFile, pdfList.get(position).getFileUrl()
                                    , pdfList.get(position).getFileTitle(), pdfList.get(position).getMime(), "Notes");
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

                            break;
                        default:
                            return false;
                    }
                    return true;
                });
                popupMenu.show();
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mime = pdfList.get(position).getMime();

                    File notesDirectory = new File(Environment.getExternalStorageDirectory() + "/Elemenophee/Notes");
                    File outputFile = new File(notesDirectory, pdfList.get(position).getFileTitle() + "." + mime.substring(mime.lastIndexOf("/") + 1));
                    Toast.makeText(context, outputFile + "", Toast.LENGTH_SHORT).show();
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

                    downloadAndStore.openFILE(outputFile, pdfList.get(position).getFileUrl()
                            , pdfList.get(position).getFileTitle(), pdfList.get(position).getMime(), "Notes");
                }
            });
        }
    }

    private void deleteContent(int position) {

        CollectionReference delRef = contentref.document(pdfList.get(position).getClas())
                .collection("/SUBJECT/")
                .document(pdfList.get(position).getSubject())
                .collection("/CONTENT/");

        delRef
                .whereEqualTo("CHAPTER", pdfList.get(position).getChapter())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        delRef.document(doc.getId()).update("NOTES", FieldValue.arrayRemove(pdfList.get(position)));
                        recentRef.document(pdfList.get(position).getTeacherID())
                                .collection("RECENT_UPLOADS")
                                .document("UPLOADS")
                                .collection("NOTES")
                                .document(pdfList.get(position).getFileTitle())
                                .delete()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfList.get(position).getFileUrl());
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
        return pdfList.size();
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
