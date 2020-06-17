package org.wazir.build.elemenophee.Teacher;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Utils.UploadUtil;

import java.util.ArrayList;


public class UploadActivity extends AppCompatActivity {

    private static final int PICK_VIDEO = 404;
    CardView uploadVideoBtn, AddExistingVideo, createNewVideo, videoSelectBtn;
    TextInputEditText videoTitle;
    Spinner selectClassVideo, selectSubjectVideo;
    Uri selectedVideoPath = null;
    String SELECTED_CHAPTER_VIDEO = "";
    String SELECTED_TEACHER_ID_VIDEO = "";
    ArrayList<addExistingModel> videoDataList = new ArrayList<>();
    private showChapterAdpater videoAlertAdapter;


    private static final int PICK_PDF = 403;
    CardView uploadPDFBtn, AddExistingPDF, createNewPDF, PDFSelectBtn;
    TextInputEditText PDFTitle;
    Spinner selectClassPDF, selectSubjectPDF;
    Uri selectedPDFPath = null;
    ArrayList<addExistingModel> pdfDataList = new ArrayList<>();
    String SELECTED_CHAPTER_NOTES = "";
    String SELECTED_TEACHER_ID_NOTES = "";
    private showChapterAdpater pdfAlertAdapter;


    ArrayAdapter<String> classSpinnerAdapter;
    ArrayAdapter<String> subjectSpinnerAdapter;
    private AlertDialog alertDialog;

    boolean addToExisting = false;
    String addExistingType;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ProgressDialog progress;

    ArrayList<String> classes, subs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        init();
        progress = new ProgressDialog(this);
        progress.setMessage("Retreiving data..");
        progress.setIndeterminate(true);

        if (classes != null && subs != null) {
            selectClassVideo.setAdapter(classSpinnerAdapter);
            selectSubjectVideo.setAdapter(subjectSpinnerAdapter);
            selectClassPDF.setAdapter(classSpinnerAdapter);
            selectSubjectPDF.setAdapter(subjectSpinnerAdapter);
        }


        createNewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNEWChapter(true);
            }
        });

        AddExistingVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExistingType = "VIDEOS";
                addToExisting = true;
                getChapters(true, false);

            }
        });

        videoSelectBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("video/*");


                @SuppressLint("IntentReset")
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_VIDEO);
            }
        });

        uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                uploadFile("VIDEOS", selectedVideoPath);
            }
        });

        createNewPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNEWChapter(false);
            }
        });

        AddExistingPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExistingType = "PDF";
                addToExisting = true;
                getChapters(false, false);
            }
        });

        PDFSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("application/pdf");


                @SuppressLint("IntentReset")
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("application/pdf");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select PDF");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_PDF);
            }
        });

        uploadPDFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile("NOTES", selectedPDFPath);

            }
        });


    }

    void uploadFile(String type, Uri selectedFilePath) {
        if (selectedFilePath != null && !selectedFilePath.toString().isEmpty()) {
            Data data;
            if (type.equals("VIDEOS")) {
                if (!videoTitle.getText().toString().isEmpty() && !SELECTED_CHAPTER_VIDEO.isEmpty()) {
                    data = new Data.Builder()
                            .putString("fileURI", selectedFilePath.toString())
                            .putBoolean("ADD_TO_EXISTING", addToExisting)
                            .putString("USER_PHONE", user.getPhoneNumber())
                            .putStringArray("FILE_INFO", new String[]{
                                    selectClassVideo.getSelectedItem() + "",
                                    selectSubjectVideo.getSelectedItem() + "",
                                    SELECTED_CHAPTER_VIDEO,
                                    videoTitle.getText() + "",
                                    type,
                                    SELECTED_TEACHER_ID_VIDEO
                            })
                            .build();
                    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadUtil.class)
                            .setInputData(data)
                            .build();

                    WorkManager.getInstance().enqueue(request);

                } else
                    Toast.makeText(getApplicationContext(), "Check Title and Chapter of VIDEO", Toast.LENGTH_SHORT).show();

            } else {

                if (!PDFTitle.getText().toString().isEmpty() && !SELECTED_CHAPTER_NOTES.isEmpty()) {
                    data = new Data.Builder()
                            .putString("fileURI", selectedFilePath.toString())
                            .putBoolean("ADD_TO_EXISTING", addToExisting)
                            .putString("USER_PHONE", user.getPhoneNumber())
                            .putStringArray("FILE_INFO", new String[]{
                                    selectClassPDF.getSelectedItem() + "",
                                    selectSubjectPDF.getSelectedItem() + "",
                                    SELECTED_CHAPTER_NOTES,
                                    PDFTitle.getText() + "",
                                    type,
                                    SELECTED_TEACHER_ID_NOTES
                            })
                            .build();

                    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadUtil.class)
                            .setInputData(data)
                            .build();

                    WorkManager.getInstance().enqueue(request);
                } else
                    Toast.makeText(getApplicationContext(), "Check Title and Chapter of PDF", Toast.LENGTH_SHORT).show();

            }
        } else
            Toast.makeText(getApplicationContext(), "SELECT FILE..", Toast.LENGTH_SHORT).show();
    }

    void createNEWChapter(final boolean isVideo) {

        getChapters(isVideo, true);

        addToExisting = false;

        AlertDialog.Builder alert = new AlertDialog.Builder(UploadActivity.this);

        alert.setTitle("Add New Chapter");
        alert.setMessage("Enter the Name of new Chapter");

        final EditText input = new EditText(UploadActivity.this);
        alert.setView(input);

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!input.getText().toString().isEmpty())
                    if (isVideo) {
                        for (addExistingModel m : videoDataList)
                            if (m.Chapter.equalsIgnoreCase(input.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "Chapter Already Exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        SELECTED_CHAPTER_VIDEO = input.getText().toString();

                    } else {
                        for (addExistingModel m : pdfDataList)
                            if (m.Chapter.equalsIgnoreCase(input.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "Chapter Already Exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        SELECTED_CHAPTER_NOTES = input.getText().toString();
                    }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_VIDEO) {
            selectedVideoPath = data.getData();
        }

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_PDF) {
            selectedPDFPath = data.getData();
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

        uploadPDFBtn = findViewById(R.id.pdfUploadBtn);
        AddExistingPDF = findViewById(R.id.addExistingPDF);
        createNewPDF = findViewById(R.id.createNewPDF);
        PDFSelectBtn = findViewById(R.id.PDFSelectBtn);
        PDFTitle = findViewById(R.id.PDFTitleUploadActivity);
        selectClassPDF = findViewById(R.id.selectClassPDFSpinner);
        selectSubjectPDF = findViewById(R.id.selectSubjectPDFSpinner);

        Intent intent = getIntent();
        classes = new ArrayList<>();
        subs = new ArrayList<>();
        classes = intent.getStringArrayListExtra("CLASSES");
        subs = intent.getStringArrayListExtra("SUBS");

        classSpinnerAdapter = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_dropdown_item, classes);
        subjectSpinnerAdapter = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_dropdown_item, subs);


    }


    private void showChapters(final boolean isVideo) {

        if (isVideo) {
            AlertDialog.Builder alt = new AlertDialog.Builder(UploadActivity.this);
            LayoutInflater inflater = LayoutInflater.from(UploadActivity.this);
            View view = inflater.inflate(R.layout.show_chapter_dialog, null);


            RecyclerView recyclerView = view.findViewById(R.id.show_chapter_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.hasFixedSize();
            recyclerView.setAdapter(videoAlertAdapter);


            alt.setView(view);
            alertDialog = alt.create();

            alertDialog.show();

        } else {
            AlertDialog.Builder alt = new AlertDialog.Builder(UploadActivity.this);
            LayoutInflater inflater = LayoutInflater.from(UploadActivity.this);
            View view = inflater.inflate(R.layout.show_chapter_dialog, null);


            RecyclerView recyclerView = view.findViewById(R.id.show_chapter_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.hasFixedSize();
            recyclerView.setAdapter(pdfAlertAdapter);


            alt.setView(view);
            alertDialog = alt.create();
            alertDialog.show();

        }

    }

    private void getChapters(final boolean isVideo, final boolean fromCreateNew) {

        CollectionReference getChapters;
        progress.show();

        if (isVideo) {
            getChapters = FirebaseFirestore.getInstance()
                    .collection("/CLASSES/" +
                            selectClassVideo.getSelectedItem().toString() +
                            "/SUBJECT/" +
                            selectSubjectVideo.getSelectedItem().toString()
                            + "/CONTENT"

                    );
            getChapters
                    .whereEqualTo("TEACHER_ID", user.getPhoneNumber())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            videoDataList.clear();
                            videoAlertAdapter = new showChapterAdpater(videoDataList, isVideo);
                            if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                    addExistingModel ad = new addExistingModel(doc.get("CHAPTER").toString(), doc.getId());
                                    videoDataList.add(ad);
                                    videoAlertAdapter.notifyDataSetChanged();
                                }

                            } else
                                Toast.makeText(getApplicationContext(), "NO CHAPTER FOUND", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            if (!fromCreateNew)
                                showChapters(isVideo);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            getChapters = FirebaseFirestore.getInstance()
                    .collection("/CLASSES/" +
                            selectClassPDF.getSelectedItem().toString() +
                            "/SUBJECT/" +
                            selectSubjectPDF.getSelectedItem().toString()
                            + "/CONTENT"

                    );
            getChapters
                    .whereEqualTo("TEACHER_ID", user.getPhoneNumber())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            pdfDataList.clear();
                            pdfAlertAdapter = new showChapterAdpater(pdfDataList, isVideo);
                            if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                    addExistingModel ad = new addExistingModel(doc.get("CHAPTER").toString(), doc.getId());
                                    pdfDataList.add(ad);
                                    pdfAlertAdapter.notifyDataSetChanged();
                                }

                            } else
                                Toast.makeText(getApplicationContext(), "NO CHAPTER FOUND", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            if (!fromCreateNew)
                                showChapters(isVideo);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    public class showChapterAdpater extends RecyclerView.Adapter<showChapterAdpater.ViewHolder> {
        ArrayList<addExistingModel> data;
        boolean isVideo;

        public showChapterAdpater(ArrayList<addExistingModel> dataList, boolean isVideo) {
            data = dataList;
            this.isVideo = isVideo;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_chapter_dialog_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.setTitle(data.get(position).Chapter);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isVideo) {
                        SELECTED_CHAPTER_VIDEO = data.get(position).Chapter;
                        SELECTED_TEACHER_ID_VIDEO = data.get(position).DOCUMENT_PATH;
                    } else {
                        SELECTED_CHAPTER_NOTES = data.get(position).Chapter;
                        SELECTED_TEACHER_ID_NOTES = data.get(position).DOCUMENT_PATH;
                    }
                    alertDialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public ConstraintLayout layout;

            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.view_chapter_item_name);
                layout = itemView.findViewById(R.id.view_chapter_item_layout);
            }

            public void setTitle(String text) {
                title.setText(text);
            }
        }

    }

    public static class addExistingModel {
        String Chapter;
        String DOCUMENT_PATH;

        public addExistingModel(String chapter, String chapter_ID) {
            Chapter = chapter;
            DOCUMENT_PATH = chapter_ID;
        }
    }
}
