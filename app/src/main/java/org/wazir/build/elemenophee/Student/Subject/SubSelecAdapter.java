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

public class SubSelecAdapter extends RecyclerView.Adapter<SubSelecAdapter.SubSelecHolder> {
    private ArrayList<ClasObjTea> objects;
    private Context context;
//    private int[] teachCount;
//    private TeaSelecInter interact;

    public SubSelecAdapter() {
    }

    public SubSelecAdapter(ArrayList<ClasObjTea> objects, Context context) {
        this.objects = objects;
        this.context = context;
//        this.interact = interact;
    }

//    public void setTeachCount(int[] teachCount) {
//        this.teachCount = teachCount;
//        notifyDataSetChanged();
//    }

//    public void setInteract(TeaSelecInter interact) {
//        this.interact = interact;
//    }

    @NonNull
    @Override
    public SubSelecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rcview_subs_layout, parent, false);
        return new SubSelecHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubSelecHolder holder, int position) {
        holder.title.setText(objects.get(position).getTitle());
//        holder.teachersCount.setVisibility(View.INVISIBLE);
        holder.rcView.setLayoutManager(new GridLayoutManager(context, 2));
        holder.rcView.setHasFixedSize(true);
//        if (teachCount != null) {
//            holder.teachersCount.setText(String.format("%s : Members", Integer.toString(teachCount[position])));
//        }
        if (objects.get(position).getExtSub() != null) {
            holder.extraSub.setText(objects.get(position).getExtSub());
        }
        SubSelecInAda adapter = new SubSelecInAda(objects.get(position).getSubs(), context);
//        adapter.setInteract(interact);
        holder.rcView.setAdapter(adapter);
        holder.clickEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.rcView.getVisibility() == View.GONE) {
                    holder.rcView.setVisibility(View.VISIBLE);
                } else {
                    holder.rcView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class SubSelecHolder extends RecyclerView.ViewHolder {
        RecyclerView rcView;
        TextView title, extraSub, teachersCount;
        ConstraintLayout clickEvent, mainLayout;

        SubSelecHolder(@NonNull View itemView) {
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
