package org.wazir.build.elemenophee;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.wazir.build.elemenophee.ModelObj.DownVideosModel;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;
import org.wazir.build.elemenophee.Utils.downloadAndStoreNotes;

import java.io.File;
import java.util.ArrayList;


class downAdapter extends RecyclerView.Adapter<downAdapter.ViewHolder> {

    Context context;
    ArrayList<File> downList;


    public downAdapter(Context context, ArrayList<File> downList) {
        this.context = context;
        this.downList = downList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upload_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(downList.get(position)).into(holder.image);
        holder.title.setText(downList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, videoPlayingActivity.class);
                intent.putExtra("VIDEO_LINK", downList.get(position).toString());
                intent.putExtra("DOWNLOADED_VIDEO_LIST", downList);
                intent.putExtra("FROM_DOWNLOADS", true);
                intent.putExtra("PLAYING_VIDEO_POSITION", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return downList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timeStamp;
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.viewUploaditemTimeStamp);
            title = itemView.findViewById(R.id.viewUploaditemTitle);
            image = itemView.findViewById(R.id.viewUploaditemImage);
        }

    }

}
