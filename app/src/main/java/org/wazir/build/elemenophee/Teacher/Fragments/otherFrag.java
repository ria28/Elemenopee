package org.wazir.build.elemenophee.Teacher.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.adapter.otherAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;

import java.util.ArrayList;


public class otherFrag extends Fragment {

    boolean fromRecent;
    RecyclerView recyclerView;
    otherAdapter otherAdapter;
    String toastMessage;

    public ArrayList<contentModel> otherList;

    public otherFrag(boolean fromRecent, ArrayList<contentModel> otherList) {
        this.fromRecent = fromRecent;
        this.otherList = otherList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_frag_layout, container, false);
        recyclerView = view.findViewById(R.id.suggestedOtherRecycler);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toastMessage = fromRecent ? "No Recent Files":"No Files found Related to this Chapter";
        if (otherList.size() != 0)
            setUpRecyclerView();
        else {
            Toast.makeText(getActivity().getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRecyclerView() {

        otherAdapter = new otherAdapter(getContext(), otherList,false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(otherAdapter);
    }
}
