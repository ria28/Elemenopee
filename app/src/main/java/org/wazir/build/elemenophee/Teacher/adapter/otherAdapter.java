package org.wazir.build.elemenophee.Teacher.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Utils.downloadAndStoreNotes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class otherAdapter extends RecyclerView.Adapter<otherAdapter.ViewHolder> {


    Context context;
    ArrayList<contentModel> otherList;


    public otherAdapter(Context context, ArrayList<contentModel> otherList) {
        this.context = context;
        this.otherList = otherList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upload_recycler_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy \nHH:mm");

        if (otherList.size() > 0) {
            holder.title.setText(otherList.get(position).getFileTitle());
            holder.timeStamp.setText(sfd.format(otherList.get(position).getTimeStamp().toDate()));
            holder.image.setImageResource(R.drawable.classroom);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mime = otherList.get(position).getMime();

                    File notesDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Elemenophee/Notes");
                    File outputFile = new File(notesDirectory,otherList.get(position).getFileTitle()+"."+mime.substring(mime.lastIndexOf("/") + 1) );
                    Toast.makeText(context,MimeTypeMap.getFileExtensionFromUrl(otherList.get(position).getFileUrl())+"",Toast.LENGTH_SHORT).show();
                    if (!notesDirectory.exists()) {
                        notesDirectory.mkdirs();
                    }

                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(25);
                    }

                    downloadAndStoreNotes downloadAndStore = downloadAndStoreNotes.getInstance(context);

                    downloadAndStore.openFILE(outputFile, otherList.get(position).getFileUrl()
                            , otherList.get(position).getFileTitle(),otherList.get(position).getMime());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return otherList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timeStamp;
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.viewUploaditemTimeStamp);
            title = itemView.findViewById(R.id.viewUploaditemTitle);
            image = itemView.findViewById(R.id.viewUploaditemImage);
        }

    }

}
