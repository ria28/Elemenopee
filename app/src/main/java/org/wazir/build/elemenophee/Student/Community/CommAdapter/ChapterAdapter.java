package org.wazir.build.elemenophee.Student.Community.CommAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.Community.Chapters;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

    private ArrayList<Chapters> data;
    private Context mContext;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.description.setText(data.get(position).getDescription());
        holder.title.setText(data.get(position).getTitle());

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
//            image =  itemView.findViewById(R.id.image_view);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION)
//                    {
//                        Chapters clickedDataItem = data.get(pos);
//                        if(pos==0)
//                        {
//
//                        }
//                        if(pos==1)
//                        {
//
//                        }
//
//                    }
//
//                }
//            });


        }
    }
}