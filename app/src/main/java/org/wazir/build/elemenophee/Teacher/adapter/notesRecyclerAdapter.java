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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Utils.downloadAndStoreNotes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class notesRecyclerAdapter extends RecyclerView.Adapter<notesRecyclerAdapter.ViewHolder> {


    Context context;
    ArrayList<contentModel> pdfList;


    public notesRecyclerAdapter(Context context, ArrayList<contentModel> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
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

        if (pdfList.size() > 0) {
            holder.title.setText(pdfList.get(position).getFileTitle());
            holder.timeStamp.setText(sfd.format(pdfList.get(position).getTimeStamp().toDate()));
            holder.image.setImageResource(R.drawable.classroom);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    File notesDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Elemenophee/Notes");
                    File outputFile = new File(notesDirectory, URLUtil.guessFileName(pdfList.get(position).getFileUrl(), null
                            , MimeTypeMap.getFileExtensionFromUrl(pdfList.get(position).getFileUrl())));
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

                    downloadAndStore.openPDF(outputFile, pdfList.get(position).getFileUrl()
                            , pdfList.get(position).getFileTitle());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
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
