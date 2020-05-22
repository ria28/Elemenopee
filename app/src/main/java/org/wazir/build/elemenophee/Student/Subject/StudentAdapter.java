package org.wazir.build.elemenophee.Student.Subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ClassObjectHolder> {
    private Context context;
    private ArrayList<ClasObjTea> objects;
//    private StuFraInt interact;
//    private int[] teachCount;

//    public void setInteract(StuFraInt interact) {
//        this.interact = interact;
//    }



    public StudentAdapter(Context context, ArrayList<ClasObjTea> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public ClassObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rcview_subs_layout, parent, false);

        return new ClassObjectHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassObjectHolder holder, int position) {
        holder.title.setText(objects.get(position).getTitle());
        holder.rcView.setLayoutManager(new GridLayoutManager(context,2));
        holder.rcView.setHasFixedSize(true);
//        if (teachCount != null) {
//            holder.teachersCount.setText(String.format("%s : Members", Integer.toString(teachCount[position])));
//        }
        if (objects.get(position).getExtSub() != null) {
            holder.extraSub.setText(objects.get(position).getExtSub());
        }
//        holder.extraSub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (holder.extraSub.getVisibility() == View.GONE) {
//                    holder.extraSub.setVisibility(View.VISIBLE);
//                } else {
//                    holder.extraSub.setVisibility(View.GONE);
//                }
//            }
//        });
        SubsAdapter adapter = new SubsAdapter(objects.get(position).getSubs(), context);
//        adapter.setInteract(interact);
        holder.rcView.setAdapter(adapter);
        holder.clickEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.rcView.getVisibility() == View.GONE) {
                    holder.rcView.setVisibility(View.VISIBLE);
                    holder.extraSub.setVisibility(View.GONE);

                } else {
                    holder.rcView.setVisibility(View.GONE);
                    holder.extraSub.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

//    public void setTeachCount(int[] teachCount) {
//        this.teachCount = teachCount;
//        notifyDataSetChanged();
//    }

    static class ClassObjectHolder extends RecyclerView.ViewHolder {
        RecyclerView rcView;
        TextView title, extraSub, teachersCount;
        ConstraintLayout clickEvent, mainLayout;

        ClassObjectHolder(@NonNull View itemView) {
            super(itemView);
            rcView = itemView.findViewById(R.id.id_rcview_subs);
            title = itemView.findViewById(R.id.textView15);
            extraSub = itemView.findViewById(R.id.textView52);
            clickEvent = itemView.findViewById(R.id.constraintLayout);
            mainLayout = itemView.findViewById(R.id.main_layout);
//            teachersCount = itemView.findViewById(R.id.textView51);
        }
    }
}

