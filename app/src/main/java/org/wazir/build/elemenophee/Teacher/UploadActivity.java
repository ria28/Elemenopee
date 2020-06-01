package org.wazir.build.elemenophee.Teacher;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Utils.UploadUtil;

import java.util.ArrayList;
import java.util.List;


public class UploadActivity extends AppCompatActivity {

    Button uploadVideoBtn, AddExistingVideo, createNewVideo, videoSelectBtn;
    TextInputEditText videoTitle;
    Spinner selectClassVideo, selectSubjectVideo;


    private static final int PICK_FILE = 101;
    Uri selectedFilePath;

    String SELECTED_CHAPTER = "";

    public static final String UPLOAD_UTIL = "UPLOAD_UTIL";


    ArrayAdapter<String> classSpinnerVideoAdapter;
    ArrayAdapter<String> subjectSpinnerVideoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        init();


        selectClassVideo.setAdapter(classSpinnerVideoAdapter);
        selectSubjectVideo.setAdapter(subjectSpinnerVideoAdapter);


        createNewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(UploadActivity.this);

                alert.setTitle("Title");
                alert.setMessage("Message");

// Set an EditText view to get user input
                final EditText input = new EditText(UploadActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SELECTED_CHAPTER = String.valueOf(input.getText());
                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        AddExistingVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChapters();
            }
        });

        videoSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("video/*");


                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_FILE);
            }
        });

        uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (!videoTitle.getText().toString().isEmpty()) {
                    Data data = new Data.Builder()
                            .putString("fileURI", selectedFilePath.toString())
                            .putStringArray("FILE_INFO", new String[]{
                                    selectClassVideo.getSelectedItem() + "", selectSubjectVideo.getSelectedItem() + "",
                                    SELECTED_CHAPTER, videoTitle.getText() + ""})
                            .build();

                    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadUtil.class)
                            .setInputData(data)
                            .build();

                    WorkManager.getInstance().enqueue(request);
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_FILE) {

            selectedFilePath = data.getData();
//            Glide.with(UploadActivity.this)
//                    .load(selectedFilePath)
//                    .into(thumbnail);

        }
    }


    void init() {
        uploadVideoBtn = findViewById(R.id.videoUploadBtn);
        AddExistingVideo = findViewById(R.id.addExistingVideo);
        createNewVideo = findViewById(R.id.createNewVideo);
        videoSelectBtn = findViewById(R.id.videoSelectBtn);
        videoTitle = findViewById(R.id.videoTitleUploadActivity);
        selectClassVideo = findViewById(R.id.selectClassVideoSpinner);
        selectSubjectVideo = findViewById(R.id.selectSubjectVideoSpinner);
        classSpinnerVideoAdapter = new ArrayAdapter<>(UploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Class 6", "Class 7", "Class 8"});

        subjectSpinnerVideoAdapter = new ArrayAdapter<>(UploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"English", "Maths", "Science"});


    }


    private void showChapters() {

        CollectionReference getChapters = FirebaseFirestore.getInstance()
                .collection("/TEACHERS/8750348232/CLASS/" +
                        selectClassVideo.getSelectedItem().toString()+
                        "/SUBJECT/" +
                        selectSubjectVideo.getSelectedItem().toString() +
                        "/CHAPTER"
                );


        getChapters.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<String> dataList = new ArrayList<>();
                if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots != null) {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        dataList.add(doc.getId());

                    }
                    AlertDialog.Builder alt = new AlertDialog.Builder(UploadActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(UploadActivity.this);
                    View view = inflater.inflate(R.layout.show_chapter_dialog, null);

                    showChapterAdpater adapter = new showChapterAdpater(dataList);

                    RecyclerView recyclerView = view.findViewById(R.id.show_chapter_recycler);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UploadActivity.this);
                    ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.hasFixedSize();
                    recyclerView.setAdapter(adapter);


                    alt.setView(view);
                    AlertDialog alertDialog = alt.create();

                    alertDialog.show();
                } else
                    Toast.makeText(getApplicationContext(), "NO CHAPTER FOUND", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class showChapterAdpater extends RecyclerView.Adapter<showChapterAdpater.ViewHolder> {
        List<String> data;

        public showChapterAdpater(List<String> dataList) {
            data = dataList;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_chapter_dialog, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.title.setText(data.get(position));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SELECTED_CHAPTER = data.get(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            ConstraintLayout layout;

            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.view_chapter_item_name);
                layout = itemView.findViewById(R.id.view_chapter_item_layout);
            }
        }

    }


}
