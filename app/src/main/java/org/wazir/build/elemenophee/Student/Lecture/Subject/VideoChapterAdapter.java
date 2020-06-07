package org.wazir.build.elemenophee.Student.Lecture.Subject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class VideoChapterAdapter extends RecyclerView.Adapter<VideoChapterAdapter.ChapterViewHolder> {

    private ArrayList<ChapterVideoObject> Chapter;
    private Context context;
    CollectionReference reference;



    public VideoChapterAdapter(Context context,ArrayList<ChapterVideoObject> Chapter) {
        this.Chapter = Chapter;
        this.context=context;
    }


    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_chapter_view, parent, false);
        return new ChapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, final int position) {

        String ChapterName= "Chapter : "+Chapter.get(position).getTitle();
        holder.title.setText(ChapterName);

        if (Chapter.get(position).getDescription() != null){
            holder.desc.setText(Chapter.get(position).getDescription());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(v.getContext(),ViewVideoListActivity.class);
                intent.putExtra("CHAPTER_TITLE",Chapter.get(position).getTitle());
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Chapter.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Chaptertitle);
            desc = itemView.findViewById(R.id.Chapterdescription);
        }
    }
}
