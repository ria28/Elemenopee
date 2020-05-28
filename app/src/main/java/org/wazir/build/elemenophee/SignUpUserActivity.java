package org.wazir.build.elemenophee;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wazir.build.elemenophee.IntLogSigScreens.ChooseAdapter;
import org.wazir.build.elemenophee.IntLogSigScreens.ChooseMoObj;
import org.wazir.build.elemenophee.Interfaces.ChooseEveHandler;

import java.util.ArrayList;

public class SignUpUserActivity extends AppCompatActivity implements ChooseEveHandler {
    ArrayList<ChooseMoObj> classes;
    ArrayList<ChooseMoObj> subjects;
    RecyclerView teaRcvClas, teaRcvSub, stuRcvClas;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        classes = new ArrayList<>();
        subjects = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        ChooseAdapter adapter = new ChooseAdapter(this, getClas());
        ChooseAdapter adapter1 = new ChooseAdapter(this, getSubs());
        adapter1.setHandler(this);
        adapter.setHandler(this);
        teaRcvClas = findViewById(R.id.recyclerView123);
        teaRcvClas.setAdapter(adapter);
        teaRcvClas.setLayoutManager(manager);
        LinearLayoutManager manager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        teaRcvSub = findViewById(R.id.recyclerView12);
        teaRcvSub.setLayoutManager(manager1);
        teaRcvSub.setAdapter(adapter1);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        stuRcvClas = findViewById(R.id.recyclerView);
        stuRcvClas.setAdapter(adapter);
        stuRcvClas.setLayoutManager(manager2);

    }

    ArrayList<ChooseMoObj> getClas() {
        ArrayList<ChooseMoObj> objs = new ArrayList<>();
        objs.add(new ChooseMoObj("5"));
        objs.add(new ChooseMoObj("6"));
        objs.add(new ChooseMoObj("7"));
        objs.add(new ChooseMoObj("8"));
        objs.add(new ChooseMoObj("9"));
        objs.add(new ChooseMoObj("10"));
        objs.add(new ChooseMoObj("11"));
        objs.add(new ChooseMoObj("12"));
        return objs;
    }

    ArrayList<ChooseMoObj> getSubs() {
        ArrayList<ChooseMoObj> objs = new ArrayList<>();
        objs.add(new ChooseMoObj("English"));
        objs.add(new ChooseMoObj("Hindi"));
        objs.add(new ChooseMoObj("Maths"));
        objs.add(new ChooseMoObj("Chemistry"));
        objs.add(new ChooseMoObj("Physics"));
        objs.add(new ChooseMoObj("Social Sc."));
        objs.add(new ChooseMoObj("Geography"));
        objs.add(new ChooseMoObj("Biology"));
        objs.add(new ChooseMoObj("Accounts"));
        objs.add(new ChooseMoObj("B. St."));
        objs.add(new ChooseMoObj("Economics"));

        return objs;
    }



    @Override
    public void selection(ChooseMoObj param, boolean action) {
        if (action) {
            if (param.getClas() != -1) {
                classes.add(param);
            } else {
                subjects.add(param);
            }
        } else {
            if (param.getClas() != -1) {
                classes.remove(param);
            } else {
                subjects.remove(param);
            }
        }
    }
}
