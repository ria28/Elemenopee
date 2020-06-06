package org.wazir.build.elemenophee.Student.Community.CommAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.Community.Chapters;
import org.wazir.build.elemenophee.Student.Community.ViewNotesActivity;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;

import java.util.ArrayList;
import java.util.Map;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

    private ArrayList<Chapters> data;
    private Context mContext;
    String title;


    public ChapterAdapter() {

    }

    public ChapterAdapter(Context mContext, ArrayList<Chapters> data) {
        this.mContext = mContext;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_chapter_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewNotesActivity.class);
                title = data.get(position).getTitle();
                intent.putExtra("CHAPTER_TITLE", title);
                mContext.startActivity(intent);
            }
        });

        holder.title.setText(title);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_comm);
            description = itemView.findViewById(R.id.description_comm);

        }
    }
}