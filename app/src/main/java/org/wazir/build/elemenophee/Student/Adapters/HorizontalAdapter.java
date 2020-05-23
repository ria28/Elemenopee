package org.wazir.build.elemenophee.Student.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.SingleHorizontal;
import org.wazir.build.elemenophee.Student.StudentActivity.ClassRoomActivity;
import org.wazir.build.elemenophee.Student.StudentActivity.LectureActivity;
import org.wazir.build.elemenophee.Student.Videos.VideoActivity;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    ArrayList<SingleHorizontal> data = new ArrayList<>();
    private Context mContext;

    public HorizontalAdapter(Context mContext, ArrayList<SingleHorizontal> data) {
        this.mContext = mContext;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.description.setText(data.get(position).getDesc());
        holder.title.setText(data.get(position).getTitle());
        holder.image.setImageResource(data.get(position).getImages());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            image =  itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        SingleHorizontal clickedDataItem = data.get(pos);
                        if(pos==0)
                        {
                            Intent intent = new Intent(mContext, LectureActivity.class);
                            mContext.startActivity(intent);
                        }
                        if(pos==1)
                        {
                            Intent intent = new Intent(mContext, ClassRoomActivity.class);
                            mContext.startActivity(intent);
                        }

                    }

                }
            });


        }
    }
}
