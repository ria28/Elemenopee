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
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.adapter.otherAdapter;

import static org.wazir.build.elemenophee.Teacher.videoPlayingActivity.pdfList;

public class notesFrag extends Fragment {

    boolean fromRecent;
    RecyclerView recyclerView;
    notesRecyclerAdapter notesAdapter;
    String toastMessage;

    public notesFrag(boolean fromRecent){
        this.fromRecent = fromRecent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_frag_layout, container, false);
        recyclerView = view.findViewById(R.id.suggestedNotesRecycler);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toastMessage = fromRecent ? "No Recent Files":"No Files found Related to this Chapter";
        if (pdfList.size() != 0)
            setUpRecyclerView();
        else {
            Toast.makeText(getActivity().getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRecyclerView() {

        notesAdapter = new notesRecyclerAdapter(getContext(), pdfList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(notesAdapter);
    }

}


