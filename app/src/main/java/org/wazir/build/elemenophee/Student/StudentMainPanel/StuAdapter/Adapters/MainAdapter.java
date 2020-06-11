package org.wazir.build.elemenophee.Student.StudentMainPanel.StuAdapter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentMainPanel.Objects.SingleVertical;

import java.util.ArrayList;

import static org.wazir.build.elemenophee.Student.StudentMainPanel.StudentMainAct.getHorizontalData;
import static org.wazir.build.elemenophee.Student.StudentMainPanel.StudentMainAct.getVerticalData;


public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> items;
    private final int VERTICAL = 1;
    private final int HORIZONTAL = 2;

    public MainAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType == VERTICAL) {
            view = inflater.inflate(R.layout.vertical, parent, false);
            return new VerticalViewHolder(view);
        }


        else {
            view = inflater.inflate(R.layout.horizontal, parent, false);
            return new HorizontalViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VERTICAL)
            verticalView((VerticalViewHolder) holder);
        else if (holder.getItemViewType() == HORIZONTAL)
            horizontalView((HorizontalViewHolder) holder);

    }

    private void verticalView(@NonNull VerticalViewHolder holder) {

        VerticalAdapter adapter1 = new VerticalAdapter(context,getVerticalData());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(adapter1);
    }


    private void horizontalView(@NonNull HorizontalViewHolder holder) {
        HorizontalAdapter adapter = new HorizontalAdapter(context,getHorizontalData());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof SingleVertical)
            return VERTICAL;
        else
            return HORIZONTAL;
    }

    public static class HorizontalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    public static class VerticalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        VerticalViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inner_recyclerView);
        }
    }

}
