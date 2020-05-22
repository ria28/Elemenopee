package org.wazir.build.elemenophee.Student.Community.CommAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.Community.SubComm;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private ArrayList<SubComm> data;
    private Context mContext;

    public SubjectAdapter(Context mContext, ArrayList<SubComm> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_subject_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.image.setImageResource(data.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
//        TextView title, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_comm);
//

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION)
//                    {
//                        SubComm clickedDataItem = data.get(pos);
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
