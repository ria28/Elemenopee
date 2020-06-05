package org.wazir.build.elemenophee.Teacher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.model.contentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class videoRecyclerAdapter extends RecyclerView.Adapter<videoRecyclerAdapter.ViewHolder> {


    Context context;
    boolean isVideoPlaying;
    ArrayList<contentModel> videoList;
    onLayoutClick onLayoutClick;
    private int selectedPosition = -1;


    public videoRecyclerAdapter(Context context, boolean isVideoPlaying, ArrayList<contentModel> videoList, onLayoutClick onLayoutClick, int selectedPosition) {
        this.context = context;
        this.isVideoPlaying = isVideoPlaying;
        this.videoList = videoList;
        this.onLayoutClick = onLayoutClick;
        this.selectedPosition = selectedPosition;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upload_recycler_item, parent, false);
        return new ViewHolder(v, onLayoutClick);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
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
//        } else {
//            holder.title.setText(pdfList.get(position).getFileTitle());
//            holder.timeStamp.setText(sfd.format(pdfList.get(position).getTimeStamp().toDate()));
//            holder.image.setImageResource(R.drawable.classroom);
//        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, timeStamp;
        public ImageView image;
        onLayoutClick onLayoutClick;


        public ViewHolder(View itemView, onLayoutClick onLayoutClick) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.viewUploaditemTimeStamp);
            title = itemView.findViewById(R.id.viewUploaditemTitle);
            image = itemView.findViewById(R.id.viewUploaditemImage);
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
