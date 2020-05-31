package org.wazir.build.elemenophee.IntLogSigScreens;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.Interfaces.ChooseEveHandler;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ChooseViewHolder> {

    private Context ctx;
    private ArrayList<ChooseMoObj> objects;
    private ChooseEveHandler handler;

    public void setHandler(ChooseEveHandler handler) {
        this.handler = handler;
    }

    public ChooseAdapter(Context ctx, ArrayList<ChooseMoObj> objects) {
        this.ctx = ctx;
        this.objects = objects;
    }

    @NonNull
    @Override
    public ChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.choose_layout, parent, false);
        return new ChooseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChooseViewHolder holder, final int position) {
        if (objects.get(position).getText().equals("")) {
            holder.displayText.setText(Integer.toString(objects.get(position).getClas()));
        } else {
            holder.displayText.setText(objects.get(position).getText());
        }

        if (objects.get(position).isState()) {
            holder.touchEve.setCardBackgroundColor(Color.parseColor("#7FFF00"));
            holder.displayText.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.touchEve.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.displayText.setTextColor(Color.parseColor("#1A2659"));
        }
        holder.touchEve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objects.get(position).isState()) {
                    holder.touchEve.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.displayText.setTextColor(Color.parseColor("#1A2659"));
                    handler.selection(objects.get(position), false);
                    ChooseMoObj temp = objects.get(position);
                    temp.setState(false);
                    objects.set(position, temp);
                } else {
                    holder.touchEve.setCardBackgroundColor(Color.parseColor("#7FFF00"));
                    holder.displayText.setTextColor(Color.parseColor("#FFFFFF"));
                    handler.selection(objects.get(position), true);
                    ChooseMoObj temp = objects.get(position);
                    temp.setState(true);
                    objects.set(position, temp);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class ChooseViewHolder extends RecyclerView.ViewHolder {
        TextView displayText;
        CardView touchEve;

        ChooseViewHolder(@NonNull View itemView) {
            super(itemView);
            displayText = itemView.findViewById(R.id.id_obj_id);
            touchEve = itemView.findViewById(R.id.id_touch_eve);
        }
    }
}
