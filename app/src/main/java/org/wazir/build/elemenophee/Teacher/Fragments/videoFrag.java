package org.wazir.build.elemenophee.Teacher.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.adapter.notesRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.notesModel;



public class videoFrag extends Fragment {


    RecyclerView recyclerView;
    notesRecyclerAdapter adapter;
    CollectionReference ref;

    public videoFrag(){}

    public videoFrag(CollectionReference ref){
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_frag_layout, container, false);
        recyclerView = view.findViewById(R.id.suggestedVideoRecycler);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Query query = ref;
        setUpRecyclerView(query);
    }

    private void setUpRecyclerView(Query q) {

        FirestoreRecyclerOptions<notesModel> options = new FirestoreRecyclerOptions.Builder<notesModel>()
                .setQuery(q, notesModel.class).build();

        adapter = new notesRecyclerAdapter(options, getContext(),ref,true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
