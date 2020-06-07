package org.wazir.build.elemenophee.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.wazir.build.elemenophee.MainActivity;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.SplashScreen;
import org.wazir.build.elemenophee.Student.Adapters.MainAdapter;

import java.util.ArrayList;

public class StudentMainAct extends AppCompatActivity {
    private ArrayList<Object> objects = new ArrayList<>();
    //    TextView textView = findViewById(R.id.heading);
    CardView cardLogout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        cardLogout = findViewById(R.id.cardView7);
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(StudentMainAct.this, SplashScreen.class));
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recycler_View);
        MainAdapter adapter = new MainAdapter(this, getObject());
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(StudentMainAct.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.id_logout) {
//            SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//
//            editor.clear();
//            editor.apply();
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
