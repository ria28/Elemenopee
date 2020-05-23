package org.wazir.build.elemenophee.Student.Community.CommAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.Community.Chapters;

import java.util.ArrayList;

import static org.wazir.build.elemenophee.Student.Community.MainCommScreen.getChapters;
import static org.wazir.build.elemenophee.Student.Community.MainCommScreen.getSubjects;

public class MainAdapterComm extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Object> items;
    private int size;
    private final int SUBJECT = 1;
    private final int CHAPTER = 2;




    public MainAdapterComm(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
//        setHasStableIds(true);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType == SUBJECT) {
            view = inflater.inflate(R.layout.subject, parent, false);
            return new SubjectViewHolder(view);
        }


        else {
            view = inflater.inflate(R.layout.chapter, parent, false);
            return new ChapterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == SUBJECT)
            subjectView((SubjectViewHolder) holder);
        else if (holder.getItemViewType() == CHAPTER)
            chapterView((ChapterViewHolder) holder);

    }

    private void subjectView(@NonNull SubjectViewHolder holder) {

        SubjectAdapter adapter1 = new SubjectAdapter(context,getSubjects());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter1);
    }


    private void chapterView(@NonNull ChapterViewHolder holder) {
        ChapterAdapter adapter = new ChapterAdapter(context,getChapters());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof Chapters){
            return CHAPTER;
        } else {
            return SUBJECT;
        }
    }


    public static class SubjectViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        SubjectViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inner_recyclerView_comm);
        }
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        ChapterViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inner_recyclerView_comm);
        }
    }
}
