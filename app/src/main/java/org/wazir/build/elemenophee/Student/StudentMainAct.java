package org.wazir.build.elemenophee.Student;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.Adapters.MainAdapter;

import java.util.ArrayList;

public class StudentMainAct extends AppCompatActivity {
    private ArrayList<Object> objects = new ArrayList<>();
//    TextView textView = findViewById(R.id.heading);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        RecyclerView recyclerView = findViewById(R.id.recycler_View);
        MainAdapter adapter = new MainAdapter(this, getObject());
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(StudentMainAct.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private ArrayList<Object> getObject() {
        objects.add(getVerticalData().get(0));
        objects.add(getHorizontalData().get(0));
        return objects;
    }

    @NonNull
    public static ArrayList<SingleVertical> getVerticalData() {
        ArrayList<SingleVertical> singleVerticals = new ArrayList<>();
        singleVerticals.add(new SingleVertical("Community", "Click here to view notes and study material available for students\nAn advanced Q/A Section for Student support", R.drawable.community));
        singleVerticals.add(new SingleVertical("Profile", "Edit or view profile here",  R.drawable.profile));
        return singleVerticals;
    }


    @NonNull
    public static ArrayList<SingleHorizontal> getHorizontalData() {
        ArrayList<SingleHorizontal> singleHorizontals = new ArrayList<>();
        singleHorizontals.add(new SingleHorizontal(R.drawable.lectures, "Lectures ", "last viewed lecture"));
        singleHorizontals.add(new SingleHorizontal(R.drawable.classroom, "Class Room", "Scheduled Lectures"));
        return singleHorizontals;
    }

}
