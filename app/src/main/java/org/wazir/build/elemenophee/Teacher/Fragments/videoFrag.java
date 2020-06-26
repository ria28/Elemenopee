package org.wazir.build.elemenophee.Teacher.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Teacher.adapter.videoRecyclerAdapter;
import org.wazir.build.elemenophee.Teacher.model.contentModel;
import org.wazir.build.elemenophee.Teacher.videoPlayingActivity;

import java.io.File;
import java.util.ArrayList;


public class videoFrag extends Fragment implements videoRecyclerAdapter.onLayoutClick {


    RecyclerView recyclerView;
    videoRecyclerAdapter adapter;

    int playingVideoPosition = 0;
    public ArrayList<contentModel> videoList = new ArrayList<>();
    public ArrayList<contentModel> pdfList = new ArrayList<>();
    public ArrayList<contentModel> otherList = new ArrayList<>();
    boolean isTeacher;
    ArrayList<File> downList;
    boolean fromDownloads = false;

    public videoFrag(int playingVideoPosition, ArrayList<contentModel> videoList,
                     ArrayList<contentModel> pdfList, ArrayList<contentModel> otherList,
                     boolean isTeacher) {
        this.playingVideoPosition = playingVideoPosition;
        this.videoList = videoList;
        this.pdfList = pdfList;
        this.otherList = otherList;
        this.isTeacher = isTeacher;
    }

    public videoFrag(int playingVideoPosition, ArrayList<File> downList, boolean fromDownloads, boolean isTeacher) {
        this.playingVideoPosition = playingVideoPosition;
        this.isTeacher = isTeacher;
        this.downList = downList;
        this.fromDownloads = fromDownloads;
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
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        if (fromDownloads)
            adapter = new videoRecyclerAdapter(getContext(), true, downList, true, this, playingVideoPosition);
        else
            adapter = new videoRecyclerAdapter(getContext(), true, videoList, this, playingVideoPosition);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClicked(int i, boolean isVideoPlaying) {
        if (!fromDownloads) {
            if (isVideoPlaying) {
                Log.d("TAG", "onClicked: True");
                Intent intent = new Intent(getContext(), videoPlayingActivity.class);
                intent.putExtra("VIDEO_LINK", videoList.get(i).getFileUrl());
                intent.putExtra("VIDEO_LIST", videoList);
                intent.putExtra("PDF_LIST", pdfList);
                intent.putExtra("OTHER_LIST", otherList);
                intent.putExtra("IS_TEACHER", isTeacher);
                intent.putExtra("PLAYING_VIDEO_POSITION", i);
                startActivity(intent);
                getActivity().finish();
            }
        } else {
            Log.d("TAG", "onClicked: False");
            Intent intent = new Intent(getContext(), videoPlayingActivity.class);
            intent.putExtra("VIDEO_LINK", downList.get(i).toString());
            intent.putExtra("DOWNLOADED_VIDEO_LIST", downList);
            intent.putExtra("FROM_DOWNLOADS", true);
            intent.putExtra("PLAYING_VIDEO_POSITION", i);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
