package org.wazir.build.elemenophee.Teacher.Fragments;


import android.content.Intent;
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
import org.wazir.build.elemenophee.Teacher.adapter.videoRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;

import static org.wazir.build.elemenophee.Teacher.videoPlayingActivity.otherList;
import static org.wazir.build.elemenophee.Teacher.videoPlayingActivity.pdfList;
import static org.wazir.build.elemenophee.Teacher.videoPlayingActivity.videoList;



public class videoFrag extends Fragment implements videoRecyclerAdapter.onLayoutClick {


    RecyclerView recyclerView;
    videoRecyclerAdapter adapter;

    int playingVideoPosition;

    public videoFrag(int playingVideoPosition){
        this.playingVideoPosition = playingVideoPosition;
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

        if (videoList.size() != 0)
            setUpRecyclerView();
        else{
            Toast.makeText(getActivity().getApplicationContext(),"No Video found Related to this Chapter",Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpRecyclerView() {


        adapter = new videoRecyclerAdapter(getContext(), true, videoList, this , playingVideoPosition);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClicked(int i, boolean isVideoPlaying) {
        if (isVideoPlaying) {
            Intent intent = new Intent(getContext(), videoPlayingActivity.class);
            intent.putExtra("VIDEO_LINK", videoList.get(i).getFileUrl());
            intent.putExtra("VIDEO_LIST", videoList);
            intent.putExtra("PDF_LIST", pdfList);
            intent.putExtra("OTHER_LIST", otherList);
            intent.putExtra("IS_TEACHER", true);
            intent.putExtra("PLAYING_VIDEO_POSITION",i);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
