package org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.Chapters;
import org.wazir.build.elemenophee.Student.Community.ViewNotesActivity;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

    private ArrayList<Chapters> data;
    private Context mContext;
    String title;


    public ChapterAdapter() {

    }

    public ChapterAdapter(Context mContext, ArrayList<Chapters> data) {
        this.mContext = mContext;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_chapter_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        title = data.get(position).getTitle();
        holder.title.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, videoPlayingActivity.class);
                intent.putExtra("CHAPTER_TITLE", title);
                intent.putExtra("SUBJECT",data.get(position).getSubName());
                intent.putExtra("CLASS",data.get(position).getClasss());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_comm);
            description = itemView.findViewById(R.id.description_comm);

        }
    }
}