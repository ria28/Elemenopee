package org.wazir.build.elemenophee.Student.Subject;
//************************************************
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class GroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
private ArrayList<Object> objects;
private Context ctx;

public void setCtx(Context ctx) {
        this.ctx = ctx;
        }

public void setObjects(ArrayList<Object> objects) {
        this.objects = objects;
        }

@Override
public int getItemViewType(int position) {
        if (objects.get(position) instanceof Title) {
        return 1;
        } else if (objects.get(position) instanceof Group) {
        return 2;
        } else {
        return -1;
        }
        }

@NonNull
@Override
public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.title_layout, parent, false);
        return new TitleHandler(v);
        } else {
        View v = LayoutInflater.from(ctx).inflate(R.layout.group_pannel, parent, false);
        return new GroupHandler(v);
        }
        }

@Override
public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHandler) {
        Title obj = (Title) objects.get(position);
        ((TitleHandler) holder).title.setText(obj.getTitle());
        } else {
        Group obj = (Group) objects.get(position);
        ((GroupHandler) holder).title.setText(obj.getTitle());
        ((GroupHandler) holder).feature1.setText(obj.getFeat1());
        ((GroupHandler) holder).feature2.setText(obj.getFeat2());
        ((GroupHandler) holder).feature3.setText(obj.getFeat3());
        ((GroupHandler) holder).author1.setText(obj.getAuthor1());
        ((GroupHandler) holder).author2.setText(obj.getAuthor2());
        ((GroupHandler) holder).category.setText(obj.getCategory());
        }
        }

@Override
public int getItemCount() {
        return objects.size();
        }

static class TitleHandler extends RecyclerView.ViewHolder {
    TextView title;

    TitleHandler(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.textView10);
    }
}

static class GroupHandler extends RecyclerView.ViewHolder {
    TextView title, feature1, feature2, feature3, author1, author2, category;

    GroupHandler(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.textView11);
        feature1 = itemView.findViewById(R.id.textView22);
        feature2 = itemView.findViewById(R.id.textView23);
        feature3 = itemView.findViewById(R.id.textView24);
        author1 = itemView.findViewById(R.id.textView26);
        author2 = itemView.findViewById(R.id.textView28);
        category = itemView.findViewById(R.id.textView21);
    }
}
}