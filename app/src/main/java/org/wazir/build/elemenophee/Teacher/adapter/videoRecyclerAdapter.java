package org.wazir.build.elemenophee.Teacher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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


    public videoRecyclerAdapter(Context context, boolean isVideoPlaying, ArrayList<contentModel> videoList, onLayoutClick onLayoutClick, int selectedPosition) {
        this.context = context;
        this.isVideoPlaying = isVideoPlaying;
        this.videoList = videoList;
        this.onLayoutClick = onLayoutClick;
        this.selectedPosition = selectedPosition;
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
            holder.options.setVisibility(View.VISIBLE);
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.options);
                    popupMenu.inflate(R.menu.video_option_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.video_download:
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
                                default:
                                    return false;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
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
            Glide.with(context)
                    .load(downList.get(position))
                    .into(holder.image);
        }
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
            options = itemView.findViewById(R.id.video_options);
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
