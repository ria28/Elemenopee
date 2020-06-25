package org.wazir.build.elemenophee;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class Downloads extends AppCompatActivity {

    RecyclerView recyclerView;
    downAdapter adapter;
    public ArrayList<File> list = new ArrayList<>();
    public String[] videoExtensions = {".mp4", ".ts", ".mkv", ".mov",
            ".3gp", ".mv2", ".m4v", ".webm", ".mpeg1", ".mpeg2", ".mts", ".ogm",
            ".bup", ".dv", ".flv", ".m1v", ".m2ts", ".mpeg4", ".vlc", ".3g2",
            ".avi", ".mpeg", ".mpg", ".wmv", ".asf"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);

        File videoDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Elemenophee/Videos/");

        adapter = new downAdapter(this, list);

        getVideos(videoDirectory);

        recyclerView = findViewById(R.id.download_recycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    private void getVideos(File directory) {
        File files[] = directory.listFiles();
        for (File file : files) {
            String name = file.getName().toLowerCase();
            for (String extension : videoExtensions) {
                if (name.endsWith(extension)) {
                    list.add(file);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }

        }
    }
}