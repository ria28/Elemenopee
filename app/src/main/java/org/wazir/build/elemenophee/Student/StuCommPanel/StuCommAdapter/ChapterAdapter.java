package org.wazir.build.elemenophee.Student.StuCommPanel.StuCommAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.Chapters;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Chapters> data;
    private ArrayList<Chapters>dataListFull;
    private Context mContext;
    String title;
    ArrayList<contentModel> videoList = new ArrayList<>();
    ArrayList<contentModel> pdfList = new ArrayList<>();
    ArrayList<contentModel> otherList = new ArrayList<>();


    public ChapterAdapter() {

    }

    public ChapterAdapter(Context mContext, ArrayList<Chapters> data) {
        this.mContext = mContext;
        this.data = data;
        dataListFull=new ArrayList<>(data);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_chapter_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        title = data.get(position).getTitle();
        holder.title.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CollectionReference reference = FirebaseFirestore.getInstance().collection("/CLASSES/" +
                        data.get(position).getClasss() +
                        "/SUBJECT/" +
                        data.get(position).getSubName() +
                        "/CONTENT"
                );
                reference
                        .whereEqualTo("TEACHER_ID", data.get(position).gettID())
                        .whereEqualTo("CHAPTER", data.get(position).getTitle())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    videoList.clear();
                                    pdfList.clear();
                                    otherList.clear();
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            if (doc.get("VIDEOS") != null) {
                                                for (Map<String, Object> obj : (ArrayList<Map<String, Object>>) doc.getData().get("VIDEOS")) {
                                                    if (obj.get("privacy").toString().equalsIgnoreCase("private")) {
                                                        if (data.get(position).getIsSubscriber())
                                                            videoList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                                    , (Timestamp) obj.get("timeStamp"), obj.get("privacy") + "", obj.get("teacherID") + "", obj.get("mime") + ""));
                                                    } else
                                                        videoList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                                , (Timestamp) obj.get("timeStamp"), obj.get("privacy") + "", obj.get("teacherID") + "", obj.get("mime") + ""));
                                                }
                                            }
                                        }
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            if (doc.get("NOTES") != null) {
                                                for (Map<String, Object> obj : (ArrayList<Map<String, Object>>) doc.getData().get("NOTES")) {
                                                    if (obj.get("privacy").toString().equalsIgnoreCase("private")) {
                                                        if (data.get(position).getIsSubscriber())
                                                            pdfList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                                    , (Timestamp) obj.get("timeStamp"), obj.get("privacy") + "", obj.get("teacherID") + "", obj.get("mime") + ""));
                                                    } else
                                                        pdfList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                                , (Timestamp) obj.get("timeStamp"), obj.get("privacy") + "", obj.get("teacherID") + "", obj.get("mime") + ""));
                                                }
                                            }
                                        }
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            if (doc.get("OTHER") != null) {
                                                for (Map<String, Object> obj : (ArrayList<Map<String, Object>>) doc.getData().get("OTHERS")) {
                                                    if (obj.get("privacy").toString().equalsIgnoreCase("private")) {
                                                        if (data.get(position).getIsSubscriber())
                                                            otherList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                                    , (Timestamp) obj.get("timeStamp"), obj.get("privacy") + "", obj.get("teacherID") + "", obj.get("mime") + ""));
                                                    } else
                                                        otherList.add(new contentModel(obj.get("fileTitle").toString(), obj.get("fileUrl").toString()
                                                                , (Timestamp) obj.get("timeStamp"), obj.get("privacy") + "", obj.get("teacherID") + "", obj.get("mime") + ""));
                                                }
                                            }
                                        }

                                        Intent intent = new Intent(mContext, videoPlayingActivity.class);
                                        if (videoList.size() > 0)
                                            intent.putExtra("VIDEO_LINK", videoList.get(position).getFileUrl());
                                        intent.putExtra("VIDEO_LIST", videoList);
                                        intent.putExtra("OTHER_LIST", otherList);
                                        intent.putExtra("PDF_LIST", pdfList);
                                        mContext.startActivity(intent);

                                    } else {
                                        Toast.makeText(mContext, "No Data found Related to this Chapter", Toast.LENGTH_SHORT).show();
                                    }

                                } else
                                    Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }
    private Filter dataFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Chapters> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Chapters item : dataListFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }else {
                        Toast.makeText(mContext,"NO such chapter",Toast.LENGTH_SHORT).show();
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_comm);
            description = itemView.findViewById(R.id.description_comm);

        }
    }
}