package org.wazir.build.elemenophee.Student.StudentSupport.Chat121;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;
import org.wazir.build.elemenophee.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mcontext;
    private List<User> mUsers;

    public UserAdapter(Context mcontext, List<User> mUsers) {
        this.mcontext = mcontext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageUrl().equals("")){
            holder.user_image.setImageResource(R.drawable.ic_person);
        }else{
            Glide.with(mcontext).load(user.getImageUrl()).into(holder.user_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mcontext,MessageActivity.class);
                intent.putExtra("user_id",user.getId());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("imageUrl",user.getImageUrl());
//                intent.putExtra("number",user.get)
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView user_image;
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_image=itemView.findViewById(R.id.user_image);
            username=itemView.findViewById(R.id.username_);
        }
    }
}
