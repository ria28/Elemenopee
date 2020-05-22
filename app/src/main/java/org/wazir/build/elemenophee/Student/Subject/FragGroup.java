package org.wazir.build.elemenophee.Student.Subject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.wazir.build.elemenophee.R;

import java.util.ArrayList;

public class FragGroup  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_group, container, false);
        RecyclerView rcview = view.findViewById(R.id.id_groups_stu);
        rcview.setHasFixedSize(true);
        rcview.setLayoutManager(new LinearLayoutManager(getContext()));
        GroupsAdapter adapter = new GroupsAdapter();
        adapter.setObjects(getData());
        adapter.setCtx(getContext());
        rcview.setAdapter(adapter);
        return view;
    }

    ArrayList<Object> getData() {
        ArrayList<Object> data = new ArrayList<>();
        data.add(new Title("Physics Groups"));
        Group g = new Group();
        g.setAuthor1("Akshay Raina");
        g.setAuthor2("Lakshay Raina");
        g.setCategory("neet");
        g.setFeat1("Revision");
        g.setFeat2("Test");
        g.setFeat3("More Test");
        g.setTitle("Erudites");
        data.add(g);
        data.add(g);
        data.add(g);
        data.add(g);
        data.add(new Title("Chemistry Groups"));
        data.add(g);
        data.add(g);
        data.add(g);
        data.add(g);
        return data;
    }
}
