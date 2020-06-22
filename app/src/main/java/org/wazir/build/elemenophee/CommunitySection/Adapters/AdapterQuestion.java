package org.wazir.build.elemenophee.CommunitySection.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.wazir.build.elemenophee.Interfaces.QuesInteract;
import org.wazir.build.elemenophee.ModelObj.QuesDispObj;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.QuesViewModel> {
    ArrayList<QuesDispObj> dataset;
    QuesInteract interact;
    Context mContext;

    public AdapterQuestion(ArrayList<QuesDispObj> dataset, Context mContext) {
        this.dataset = dataset;
        this.mContext = mContext;
    }

    public void setInteract(QuesInteract interact) {
        this.interact = interact;
    }

    public void setDataset(ArrayList<QuesDispObj> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuesViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ques_layout, parent, false);
        return new QuesViewModel(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuesViewModel holder, final int position) {
        holder.name.setText(dataset.get(position).getStuName());
        holder.ques.setText(dataset.get(position).getQuestion());
        holder.date.setText(dataset.get(position).getDate());
        if (dataset.get(position).getUpVotes() > 0) {
            holder.likes.setVisibility(View.VISIBLE);
            holder.likes.setText("" + dataset.get(position).getUpVotes());
        }
        Glide.with(mContext).load(dataset.get(position).getStuProPic()).into(holder.profilePic);

        holder.voteQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.voteQuestion(dataset.get(position).getQuesId(), position);
            }
        });

        holder.viewQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.viewQuestion(dataset.get(position).getQuesId(), position);
            }
        });

        holder.solveQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.giveSolution(dataset.get(position).getQuesId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    class QuesViewModel extends RecyclerView.ViewHolder {
        CircleImageView profilePic;
        TextView name, date, ques;
        TextView likes, answers;
        LinearLayout voteQues, solveQues, viewQues;

        public QuesViewModel(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.circleImageView2);
            name = itemView.findViewById(R.id.textView43);
            date = itemView.findViewById(R.id.textView44);
            ques = itemView.findViewById(R.id.textView46);
            voteQues = itemView.findViewById(R.id.linearLayout3);
            solveQues = itemView.findViewById(R.id.linearLayout6);
            viewQues = itemView.findViewById(R.id.linearLayout7);
            likes = itemView.findViewById(R.id.textView45);
        }
    }
}
