package org.wazir.build.elemenophee.Student.StudentSubscription;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

import org.wazir.build.elemenophee.ModelObj.TeacherObj;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.ViewTeacherProfile;

import java.util.ArrayList;

public class StuTeacherAdapter extends FirestorePagingAdapter<TeacherObj, StuTeacherAdapter.MyViewHolder> {
    Context context;

    public StuTeacherAdapter(@NonNull FirestorePagingOptions<TeacherObj> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull final TeacherObj model) {

        holder.name.setText(model.getName());
        holder.description.setText(model.getExperience());
        if (model.getProPicURL() != null && !model.getProPicURL().isEmpty())
            Glide.with(context).load(model.getProPicURL()).into(holder.profileImage);
        else
            Glide.with(context).load(context.getDrawable(R.drawable.profile_dummy)).into(holder.profileImage);
        holder.view_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewTeacherProfile.class);
                intent.putExtra("TEACHER_ID", model.getPhone());
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_teacher_rv, parent, false);
        return new MyViewHolder(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView name, view_Profile;
        public TextView description;

        public MyViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.teacherPic);
            name = itemView.findViewById(R.id.teacher_name);
            description = itemView.findViewById(R.id.teacher_description);
            view_Profile = itemView.findViewById(R.id.viewProfileText);

        }
    }
}