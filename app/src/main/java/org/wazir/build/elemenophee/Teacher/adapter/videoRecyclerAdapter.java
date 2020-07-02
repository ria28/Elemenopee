package org.wazir.build.elemenophee.Teacher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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


public class videoRecyclerAdapter extends RecyclerView.Adapter<videoRecyclerAdapter.ViewHolder> {


    Context context;
    boolean isVideoPlaying;
    ArrayList<contentModel> videoList;
    ArrayList<File> downList;
    onLayoutClick onLayoutClick;
    private int selectedPosition = -1;
    boolean fromDownloads = false;
    AlertDialog.Builder builder;
    AlertDialog alert;
    StorageReference storageReference;
    boolean fromViewUploads = false;

    CollectionReference contentref = FirebaseFirestore.getInstance().collection("/CLASSES/");
    CollectionReference recentRef = FirebaseFirestore.getInstance().collection("/TEACHERS/");


    public videoRecyclerAdapter(Context context, boolean isVideoPlaying, ArrayList<contentModel> videoList, onLayoutClick onLayoutClick, int selectedPosition, boolean fromViewUpload) {
        this.context = context;
        this.isVideoPlaying = isVideoPlaying;
        this.videoList = videoList;
        this.onLayoutClick = onLayoutClick;
        this.selectedPosition = selectedPosition;
        builder = new AlertDialog.Builder(context);
        this.fromViewUploads = fromViewUpload;
    }

    public videoRecyclerAdapter(Context context, boolean isVideoPlaying, ArrayList<File> downList, boolean fromDownloads, onLayoutClick onLayoutClick, int selectedPosition) {
        this.context = context;
        this.isVideoPlaying = isVideoPlaying;
        this.downList = downList;
        this.onLayoutClick = onLayoutClick;
        this.selectedPosition = selectedPosition;
        this.fromDownloads = fromDownloads;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upload_recycler_item, parent, false);
        return new ViewHolder(v, onLayoutClick);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if(!fromViewUploads){

        }

        if (!fromDownloads) {
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy \nHH:mm");

            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5"));
                holder.itemView.setElevation(0);
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.itemView.setElevation(4);
            }

            holder.title.setText(videoList.get(position).getFileTitle());
            holder.timeStamp.setText(sfd.format(videoList.get(position).getTimeStamp().toDate()));
            Glide.with(context)
                    .load(videoList.get(position).getFileUrl())
                    .into(holder.image);
            holder.options.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, holder.options);
                popupMenu.inflate(R.menu.video_option_menu);
                if(!fromViewUploads){
                    popupMenu.getMenu().findItem(R.id.deleteOption).setVisible(false);
                }
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.downloadOption:
                            String mime = videoList.get(position).getMime();
                            File notesDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Elemenophee/Videos");
                            File outputFile = new File(notesDirectory, videoList.get(position).getFileTitle() + "." + mime.substring(mime.lastIndexOf("/") + 1));
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

                            downloadAndStore.openFILE(outputFile, videoList.get(position).getFileUrl()
                                    , videoList.get(position).getFileTitle(), videoList.get(position).getMime(), "Videos");
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
        } else {
            Log.d("TAG", "onBindViewHolder: " + downList.size());
            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5"));
                holder.itemView.setElevation(0);
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.itemView.setElevation(4);
            }
            holder.title.setText(downList.get(position).getName());
            holder.timeStamp.setText("");
            holder.options.setVisibility(View.GONE);
            Glide.with(context)
                    .load(downList.get(position))
                    .into(holder.image);
        }
    }

    private void deleteContent(int position) {

        CollectionReference delRef = contentref.document(videoList.get(position).getClas())
                .collection("/SUBJECT/")
                .document(videoList.get(position).getSubject())
                .collection("/CONTENT/");

        delRef
                .whereEqualTo("CHAPTER", videoList.get(position).getChapter())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        contentModel model = videoList.get(position);
                        delRef.document(doc.getId()).update("VIDEOS", FieldValue.arrayRemove(model));
                        recentRef.document(videoList.get(position).getTeacherID())
                                .collection("RECENT_UPLOADS")
                                .document("UPLOADS")
                                .collection("VIDEOS")
                                .document(videoList.get(position).getFileTitle())
                                .delete()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        recentRef.document(videoList.get(position).getTeacherID())
                                                .update("videoCount", FieldValue.increment(-1))
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(videoList.get(position).getFileUrl());
                                                        storageReference
                                                                .delete()
                                                                .addOnSuccessListener(aVoid -> {
                                                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                                }).addOnFailureListener(exception -> {
                                                            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                                    } else
                                                        Toast.makeText(context, task2.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        return fromDownloads ? downList.size() : videoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, timeStamp;
        public ImageView image, options;
        onLayoutClick onLayoutClick;


        public ViewHolder(View itemView, onLayoutClick onLayoutClick) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.viewUploaditemTimeStamp);
            title = itemView.findViewById(R.id.viewUploaditemTitle);
            image = itemView.findViewById(R.id.viewUploaditemImage);
            options = itemView.findViewById(R.id.imageView10);
            this.onLayoutClick = onLayoutClick;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLayoutClick.onClicked(getAdapterPosition(), isVideoPlaying);
        }
    }


    public interface onLayoutClick {
        void onClicked(int i, boolean isVideoPlaying);
    }


}
