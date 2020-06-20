package org.wazir.build.elemenophee.Student.StudentSubscription;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class StuTeacherAdapter extends RecyclerView.Adapter<StuTeacherAdapter.MyViewHolder> {
    private ArrayList<TeacherObj> TeacherList;


    public StuTeacherAdapter(ArrayList<TeacherObj> exampleList) {
        this.TeacherList = exampleList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_teacher_rv, parent, false);
        MyViewHolder evh = new MyViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        for(int i=0;i<1;i++)
            System.out.println(i);
        TeacherObj currentItem = TeacherList.get(position);;
        holder.name.setText(currentItem.getTeacherName());

        holder.description.setText(currentItem.getDescription());

    }

    @Override
    public int getItemCount() {
        return TeacherList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView name;
        public TextView description;
        public MyViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.teacherPic);
            name = itemView.findViewById(R.id.teacher_name);
            description = itemView.findViewById(R.id.teacher_description);
        }
    }
}