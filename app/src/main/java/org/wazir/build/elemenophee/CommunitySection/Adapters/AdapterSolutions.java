package org.wazir.build.elemenophee.CommunitySection.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.wazir.build.elemenophee.ModelObj.AnsObj;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSolutions extends RecyclerView.Adapter<AdapterSolutions.SolutionViewHolder> {

    ArrayList<AnsObj> solutions;
    Context mCtx;

    public AdapterSolutions(ArrayList<AnsObj> solutions, Context mCtx) {
        this.solutions = solutions;
        this.mCtx = mCtx;
    }


    @NonNull
    @Override
    public SolutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.solution_layout, parent, false);
        return new SolutionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionViewHolder holder, int position) {
        holder.solution.setText(solutions.get(position).getAnswer());
        holder.time.setText(solutions.get(position).getTime());
        holder.name.setText(solutions.get(position).getUserName());
        Glide.with(mCtx).load(solutions.get(position).getUserProPic()).into(holder.proPic);
    }

    @Override
    public int getItemCount() {
        return solutions.size();
    }

    class SolutionViewHolder extends RecyclerView.ViewHolder {
        CircleImageView proPic;
        TextView name, solution, time;

        public SolutionViewHolder(@NonNull View itemView) {
            super(itemView);
            proPic = itemView.findViewById(R.id.circleImageView3);
            name = itemView.findViewById(R.id.textView48);
            solution = itemView.findViewById(R.id.textView49);
            time = itemView.findViewById(R.id.textView54);
        }
    }
}
