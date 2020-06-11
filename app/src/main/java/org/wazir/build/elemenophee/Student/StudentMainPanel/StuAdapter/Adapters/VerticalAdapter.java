package org.wazir.build.elemenophee.Student.StudentMainPanel.StuAdapter.Adapters;

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
import org.wazir.build.elemenophee.Student.StudentMainPanel.Objects.SingleVertical;
import org.wazir.build.elemenophee.Student.StuCommPanel.Stu_main_comm_screen;
import org.wazir.build.elemenophee.Student.StuProfile.ProfileMainActivity;
import org.wazir.build.elemenophee.Student.StudentProfile.StudentProfileActivity;

import java.util.ArrayList;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {
     ArrayList<SingleVertical> data = new ArrayList<>();
     private Context mContext;

    public VerticalAdapter(Context mContext, ArrayList<SingleVertical> data) {
        this.data = data;
        this.mContext=mContext;
        //gygiugiu
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.image.setImageResource(data.get(position).getImage());
        holder.title.setText(data.get(position).getHeader());
        holder.description.setText(data.get(position).getSubHeader());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        SingleVertical clickedDataItem = data.get(pos);
                        if(pos==0)
                        {
                            Intent intent = new Intent(mContext, Stu_main_comm_screen.class);
                            mContext.startActivity(intent);
                        }
                        if(pos==1)
                        {
                            Intent intent = new Intent(mContext, StudentProfileActivity.class);
                            mContext.startActivity(intent);
                        }

                    }

                }
            });
        }
    }
}

