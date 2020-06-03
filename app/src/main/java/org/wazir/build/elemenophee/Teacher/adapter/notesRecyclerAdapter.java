package org.wazir.build.elemenophee.Teacher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.notesModel;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;


public class notesRecyclerAdapter extends FirestoreRecyclerAdapter<notesModel, notesRecyclerAdapter.ViewHolder> {

    Context context;
    CollectionReference ref;
    boolean isVideoPlaying = false;

    public notesRecyclerAdapter(@NonNull FirestoreRecyclerOptions<notesModel> options, Context context, CollectionReference ref, boolean isVideoPlaying) {
        super(options);
        this.context = context;
        this.ref = ref;
        this.isVideoPlaying = isVideoPlaying;
    }


    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull final notesModel notesModel) {
        viewHolder.title.setText(notesModel.getFileTitle());
        Glide.with(context)
                .load(notesModel.getFileUrl())
                .into(viewHolder.image);

        if (!isVideoPlaying) {
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, videoPlayingActivity.class);
                    intent.putExtra("NOTES_LINK", notesModel.getFileUrl());
                    context.startActivity(intent);


                }
            });
        }else if (isVideoPlaying){
            videoPlayingActivity.notes_link = notesModel.getFileUrl();
            videoPlayingActivity.changeTriggered();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upload_recycler_item, parent, false);
        return new ViewHolder(v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.viewUploaditemTitle);
            image = itemView.findViewById(R.id.viewUploaditemImage);
            layout = itemView.findViewById(R.id.viewUploadItemLayout);
        }
    }


}
