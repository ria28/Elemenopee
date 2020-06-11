package org.wazir.build.elemenophee.Student.Lecture.Subject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.Lecture.Subject.Adapter.StudentAdapter;
import org.wazir.build.elemenophee.Student.Lecture.Subject.Object.ClasObjTea;
import org.wazir.build.elemenophee.Student.Lecture.Subject.Object.SubObj;

import java.util.ArrayList;

public class FragMainStu extends Fragment {
    private View view;
//    private StuFraInt interact;

//    public void setInteract(StuFraInt interact) {
//        this.interact = interact;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_main_stu, container, false);
        rcViewSetup();
        return view;
    }

    private void rcViewSetup() {
        RecyclerView rcView = view.findViewById(R.id.id_stu_main_rcview);
        rcView.setHasFixedSize(true);
        rcView.setLayoutManager(new LinearLayoutManager(getContext()));
        StudentAdapter adapter = new StudentAdapter(getContext(), getDataForDisplay());
//        adapter.setTeachCount(getFacultyCounter());
//        adapter.setInteract(interact);
        rcView.setAdapter(adapter);
    }

//    private int[] getFacultyCounter() {
//        return new int[]{45, 56, 67, 45, 11, 2, 59, 8, 1, 32, 65, 45};
//    }

    private ArrayList<ClasObjTea> getDataForDisplay() {
        ArrayList<ClasObjTea> arrayList = new ArrayList<>();
        ArrayList<SubObj> subs = new ArrayList<>();
        subs.add(new SubObj("5M", "Maths"));
        subs.add(new SubObj("5S", "Science"));
        subs.add(new SubObj("5E", "English"));
        subs.add(new SubObj("5ES", "EVS"));
        ClasObjTea c5 = new ClasObjTea("5", subs);
        arrayList.add(c5);

        ArrayList<SubObj> subs6 = new ArrayList<>();
        subs6.add(new SubObj("6M", "Maths"));
        subs6.add(new SubObj("6S", "Science"));
        subs6.add(new SubObj("6E", "English"));
        subs6.add(new SubObj("6ES", "EVS"));
        subs6.add(new SubObj("6SS", "SST"));
        subs6.add(new SubObj("6GK", "GK"));
        ClasObjTea c6 = new ClasObjTea("6", subs6);
        arrayList.add(c6);

        ArrayList<SubObj> subs7 = new ArrayList<>();
        subs7.add(new SubObj("7M", "Maths"));
        subs7.add(new SubObj("7S", "Science"));
        subs7.add(new SubObj("7E", "English"));
        subs7.add(new SubObj("7ES", "EVS"));
        subs7.add(new SubObj("7SS", "SST"));
        subs7.add(new SubObj("7GK", "GK"));
        ClasObjTea c7 = new ClasObjTea("7", subs7);
        arrayList.add(c7);

        ArrayList<SubObj> subs8 = new ArrayList<>();
        subs8.add(new SubObj("8M", "Maths"));
        subs8.add(new SubObj("8S", "Science"));
        subs8.add(new SubObj("8E", "English"));
        subs8.add(new SubObj("8ES", "EVS"));
        subs8.add(new SubObj("8SS", "SST"));
        subs8.add(new SubObj("8GK", "GK"));
        ClasObjTea c8 = new ClasObjTea("8", subs8);
        arrayList.add(c8);

        ArrayList<SubObj> subs9 = new ArrayList<>();
        subs9.add(new SubObj("9M", "Maths"));
        subs9.add(new SubObj("9H", "History"));
        subs9.add(new SubObj("9EL", "English Lit."));
        subs9.add(new SubObj("9EG", "English Gram."));
        subs9.add(new SubObj("9P", "Physics"));
        subs9.add(new SubObj("9C", "Chemistry"));
        subs9.add(new SubObj("9B", "Biology"));
        ClasObjTea c9 = new ClasObjTea("9", subs9);
        arrayList.add(c9);

        ArrayList<SubObj> subs10 = new ArrayList<>();
        subs10.add(new SubObj("10M", "Maths"));
        subs10.add(new SubObj("10SS", "S. Sc."));
        subs10.add(new SubObj("10S", "Science"));
        subs10.add(new SubObj("10EG", "English"));
        ClasObjTea c10 = new ClasObjTea("10", subs10);
        arrayList.add(c10);

        ArrayList<SubObj> subs11s = new ArrayList<>();
        subs11s.add(new SubObj("11sM", "Maths"));
        subs11s.add(new SubObj("11sP", "Physics"));
        subs11s.add(new SubObj("11sC", "Chemistry"));
        subs11s.add(new SubObj("11sB", "Biology"));
        subs11s.add(new SubObj("11sE", "English"));
        ClasObjTea c11s = new ClasObjTea("11", "Science", subs11s);
        arrayList.add(c11s);

        ArrayList<SubObj> subs11c = new ArrayList<>();
        subs11c.add(new SubObj("11cM", "Maths"));
        subs11c.add(new SubObj("11cA", "Accountancy"));
        subs11c.add(new SubObj("11cB", "Business Stu."));
        subs11c.add(new SubObj("11cE", "Economics"));
        subs11c.add(new SubObj("11cE", "English"));
        ClasObjTea c11c = new ClasObjTea("11", "Commerce", subs11c);
        arrayList.add(c11c);

        ArrayList<SubObj> subs12s = new ArrayList<>();
        subs12s.add(new SubObj("12sM", "Maths"));
        subs12s.add(new SubObj("12sP", "Physics"));
        subs12s.add(new SubObj("12sC", "Chemistry"));
        subs12s.add(new SubObj("12sB", "Biology"));
        subs12s.add(new SubObj("12sE", "English"));
        ClasObjTea c12s = new ClasObjTea("12", "Science", subs12s);
        arrayList.add(c12s);

        ArrayList<SubObj> subs12c = new ArrayList<>();
        subs12c.add(new SubObj("12cM", "Maths"));
        subs12c.add(new SubObj("12cA", "Accountancy"));
        subs12c.add(new SubObj("12cB", "Business Stu."));
        subs12c.add(new SubObj("12cE", "Economics"));
        subs12c.add(new SubObj("12cE", "English"));
        ClasObjTea c12c = new ClasObjTea("12", "Commerce", subs12c);
        arrayList.add(c12c);


        return arrayList;
    }
}
