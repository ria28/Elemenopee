package org.wazir.build.elemenophee.CommunitySection;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.LoadingPopup;
import org.wazir.build.elemenophee.R;

public class ComPanActivity extends AppCompatActivity {
    Spinner claSpiMain, subSpiMain;
    String claSpiMainStr, subSpiMainStr;
    CardView raiseQuestionCard;
    LoadingPopup loading;
    RecyclerView commRcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_pan);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initLayout();
        initiateRcView();
    }

    void initLayout() {
        claSpiMain = findViewById(R.id.spinner4);
        subSpiMain = findViewById(R.id.spinner3);
        loading = new LoadingPopup(this);

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this, R.array.classes_array, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        claSpiMain.setAdapter(classAdapter);

        claSpiMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                claSpiMainStr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> subAdapterMain = ArrayAdapter.createFromResource(this, R.array.subs_array, android.R.layout.simple_spinner_item);
        subAdapterMain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpiMain.setAdapter(subAdapterMain);

        subSpiMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subSpiMainStr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        raiseQuestionCard = findViewById(R.id.id_raise_ques);
    }

    void initiateRcView() {
        commRcView = findViewById(R.id.community_main_screen_ID);
        commRcView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    public void askQues(View view) {
        loading.askQuestionPopup();
    }
}