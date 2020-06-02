package org.wazir.build.elemenophee.Student.Community;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.Community.CommAdapter.MainAdapterComm;

import java.util.ArrayList;

public class MainCommScreen extends AppCompatActivity {

    private ArrayList<Object> objects = new ArrayList<>();

//    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comm_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        RecyclerView recyclerView = findViewById(R.id.recycler_View_comm);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainCommScreen.this));
        recyclerView.setAdapter(new MainAdapterComm(MainCommScreen.this, getObject()));
    }

    private ArrayList<Object> getObject() {

        objects.add(getSubjects().get(0));
//        n = list1.size();
        objects.add(getChapters().get(0));
        return objects;
    }

    @NonNull
    public static ArrayList<SubComm> getSubjects() {
        ArrayList<SubComm> list1 = new ArrayList<>();
        list1.add(new SubComm(R.drawable.maths));
        list1.add(new SubComm(R.drawable.sci));
        list1.add(new SubComm(R.drawable.sst));
        list1.add(new SubComm(R.drawable.maths));
        list1.add(new SubComm(R.drawable.sci));
        list1.add(new SubComm(R.drawable.sst));

        return list1;
    }


    @NonNull
    public static ArrayList<Chapters> getChapters() {
        ArrayList<Chapters> list2 = new ArrayList<>();
        list2.add(new Chapters("1. Laws Of Motion ", "A Brief Description of the Chapter over here\n" +
                "which will give a glimpse of the chapter to the\n" +
                "Reader and make it look long "));

        list2.add(new Chapters("1. Laws Of Motion ", "A Brief Description of the Chapter over here\n" +
                "which will give a glimpse of the chapter to the\n" +
                "Reader and make it look long "));

        list2.add(new Chapters("1. Laws Of Motion ", "A Brief Description of the Chapter over here\n" +
                "which will give a glimpse of the chapter to the\n" +
                "Reader and make it look long "));

        list2.add(new Chapters("1. Laws Of Motion ", "A Brief Description of the Chapter over here\n" +
                "which will give a glimpse of the chapter to the\n" +
                "Reader and make it look long "));
        list2.add(new Chapters("1. Laws Of Motion ", "A Brief Description of the Chapter over here\n" +
                "which will give a glimpse of the chapter to the\n" +
                "Reader and make it look long "));
        list2.add(new Chapters("1. Laws Of Motion ", "A Brief Description of the Chapter over here\n" +
                "which will give a glimpse of the chapter to the\n" +
                "Reader and make it look long "));
        list2.add(new Chapters("1. Laws Of Motion ", "A Brief Description of the Chapter over here\n" +
                "which will give a glimpse of the chapter to the\n" +
                "Reader and make it look long "));
        return list2;
    }
}
