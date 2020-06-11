package org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.SubComm;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private ArrayList<SubComm> data;
    private Context mContext;
    private OnSubjListener mOnSubjListener;

    public SubjectAdapter(Context mContext, ArrayList<SubComm> data,  OnSubjListener onSubjListener) {
        this.data = data;
        this.mContext = mContext;
        this.mOnSubjListener=onSubjListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_subject_single_row, parent, false);
        return new MyViewHolder(view,mOnSubjListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.image.setImageResource(data.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        OnSubjListener onSubjListener;
        TextView title;

        public MyViewHolder(View itemView,OnSubjListener onSubjListener) {
            super(itemView);
            image = itemView.findViewById(R.id.image_comm);
            this.onSubjListener=onSubjListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnSubjListener.onSubjClick(getAdapterPosition());
        }
    }

    public  interface OnSubjListener{
       void onSubjClick(int position);
    }
}
