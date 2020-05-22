package org.wazir.build.elemenophee.Student.Subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class SubsAdapter extends RecyclerView.Adapter<SubsAdapter.SubsViewHolder> {
    private ArrayList<SubObj> subs;
    private Context ctx;

//    private StuFraInt interact;

//    public void setInteract(StuFraInt interact) {
//        this.interact = interact;
//    }

    SubsAdapter(ArrayList<SubObj> subs, Context ctx) {
        this.subs = subs;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public SubsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.subs_layout, parent, false);
        return new SubsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubsViewHolder holder, final int position) {
        holder.sub.setText(subs.get(position).getSubTitle());
        holder.subs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                interact.showTeachers(subs.get(position).getSubId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return subs.size();
    }


    static class SubsViewHolder extends RecyclerView.ViewHolder {
        TextView sub;
        CardView subs;

        SubsViewHolder(@NonNull View itemView) {
            super(itemView);
            sub = itemView.findViewById(R.id.textView16);
            subs = itemView.findViewById(R.id.card_subs);
        }
    }
}

