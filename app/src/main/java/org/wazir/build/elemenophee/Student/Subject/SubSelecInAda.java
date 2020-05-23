package org.wazir.build.elemenophee.Student.Subject;
//*********************************************************************************
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class SubSelecInAda extends RecyclerView.Adapter<SubSelecInAda.SubSelecInHolder> {
    private ArrayList<SubObj> subs;
    private Context ctx;
//    private TeaSelecInter interact;

//    public void setInteract(TeaSelecInter interact) {
//        this.interact = interact;
//    }

    SubSelecInAda(ArrayList<SubObj> subs, Context ctx) {
        this.subs = subs;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public SubSelecInAda.SubSelecInHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.subs_layout, parent, false);
        return new SubSelecInHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubSelecInHolder holder, final int position) {
        holder.sub.setText(subs.get(position).getSubTitle());
        holder.subs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!subs.get(position).isClicked()) {
                    view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color2));
                    holder.sub.setTextColor(ContextCompat.getColor(ctx, R.color.color4));
//                    interact.addSub(subs.get(position).getSubId());
                    subs.get(position).setClicked(true);
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color5));
                    holder.sub.setTextColor(ContextCompat.getColor(ctx, R.color.color3));
//                    interact.removeSub(subs.get(position).getSubId());
                    subs.get(position).setClicked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subs.size();
    }


    static class SubSelecInHolder extends RecyclerView.ViewHolder {
        TextView sub;
        CardView subs;

        SubSelecInHolder(@NonNull View itemView) {
            super(itemView);
            sub = itemView.findViewById(R.id.textView16);
            subs = itemView.findViewById(R.id.card_subs);
        }
    }
}
