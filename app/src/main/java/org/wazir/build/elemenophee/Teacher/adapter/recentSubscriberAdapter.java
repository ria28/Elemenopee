package org.wazir.build.elemenophee.Teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.wazir.build.elemenophee.ModelObj.StudentObj;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class recentSubscriberAdapter extends RecyclerView.Adapter<recentSubscriberAdapter.ViewHolder> {
    ArrayList<StudentObj> list;
    Context context;

    public recentSubscriberAdapter(ArrayList<StudentObj> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_subs_item_teacher, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).getmImageUrl() != null && !list.get(position).getmImageUrl().isEmpty())
            Glide.with(context).load(list.get(position).getmImageUrl()).into(holder.image);
        else
            Glide.with(context).load(context.getDrawable(R.drawable.img_intro_4)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recentSubsPic);
        }
    }
}
