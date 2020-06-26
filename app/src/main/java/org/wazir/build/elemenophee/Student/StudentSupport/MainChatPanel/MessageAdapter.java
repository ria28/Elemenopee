package org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.wazir.build.elemenophee.R;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MessObj> messages;
    private Context ctx;
    private FirebaseAuth mAuth;
    FirebaseStorage mStorageRef;

    public MessageAdapter(ArrayList<MessObj> messages, Context ctx) {
        this.messages = messages;
        this.ctx = ctx;
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance();
    }

    public void setMessages(ArrayList<MessObj> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public ArrayList<MessObj> getMessages() {
        return messages;
    }


    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderId().equals(mAuth.getCurrentUser().getPhoneNumber())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.self_message_format, parent, false);
            return new SenderViewHolder(v);
        } else {
            View v = LayoutInflater.from(ctx).inflate(R.layout.message_layout, parent, false);
            return new ReceiverViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReceiverViewHolder) {
//            ((ReceiverViewHolder) holder).name.setText(messages.get(position).getSendName());
            ((ReceiverViewHolder) holder).message.setText(messages.get(position).getMessage());
            if (!messages.get(position).getImageUrl().equals("")) {
                Glide.with(ctx).load(messages.get(position).getImageUrl()).into(((ReceiverViewHolder) holder).imageView);
                ((ReceiverViewHolder) holder).imageView.setVisibility(View.VISIBLE);
            }
        } else {
            ((SenderViewHolder) holder).message.setText(messages.get(position).getMessage());
            if (!messages.get(position).getImageUrl().equals("")) {
                Glide.with(ctx).load(messages.get(position).getImageUrl()).into(((SenderViewHolder) holder).image);
                ((SenderViewHolder) holder).image.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView name, message;
        ImageView imageView;

        ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
//            name = itemView.findViewById(R.id.textView78);
            message = itemView.findViewById(R.id.textView79);
            imageView = itemView.findViewById(R.id.imageView3);
        }
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        ImageView image;

        SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.textView81);
            image = itemView.findViewById(R.id.imageView14);
        }
    }
}
