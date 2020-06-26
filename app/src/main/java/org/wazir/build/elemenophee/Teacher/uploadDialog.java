package org.wazir.build.elemenophee.Teacher;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Utils.UploadUtil;

import java.util.ArrayList;

class uploadDialog {
    showChapterAdpater fileAdapter;
    boolean addToExisting = false;
    AlertDialog uploadDialog;
    AlertDialog showChapterDialog;
    Context context;
    String UploadType;
    ArrayList<String> classes, subjects;
    String SELECTED_CHAPTER = "";
    String SELECTED_TEACHER_ID = "";
    private Uri selectedFilePath;
    FirebaseUser user;


    private ArrayList<addExistingModel> dataList = new ArrayList<>();


    public uploadDialog(Context context, String type, ArrayList<String> classes, ArrayList<String> subjects, Uri selectedFilePath, FirebaseUser user) {
        this.context = context;
        UploadType = type;
        this.classes = classes;
        this.subjects = subjects;
        this.selectedFilePath = selectedFilePath;
        this.user = user;
    }


    public void openDialog() {

        fileAdapter = new uploadDialog.showChapterAdpater(dataList);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final View view1 = LayoutInflater.from(context).inflate(R.layout.upload_dialog, null);
        alert.setView(view1);
        ConstraintLayout uploadBtn = view1.findViewById(R.id.uploadBtn);
        TextView title = view1.findViewById(R.id.titleUploadDialog);
        CardView addExisting = view1.findViewById(R.id.addExisting);
        CardView createNew = view1.findViewById(R.id.createNew);
        final Switch privacy = view1.findViewById(R.id.uploadPrivacy);
        final Spinner selectClass = view1.findViewById(R.id.selectClassSpinner);
        final Spinner selectSubject = view1.findViewById(R.id.selectSubjectSpinner);
        final TextInputEditText fileTitle = view1.findViewById(R.id.uploadTitle);

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, classes);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, subjects);


        selectClass.setAdapter(classAdapter);
        selectSubject.setAdapter(subjectAdapter);

        getChapters(selectClass.getSelectedItem() + "", selectSubject.getSelectedItem() + "");


        title.setText("Upload " + UploadType);

        uploadBtn.setOnClickListener(v -> {
            if (selectedFilePath != null && !selectedFilePath.toString().isEmpty()) {
                Data data;
                if (!fileTitle.getText().toString().isEmpty() && !SELECTED_CHAPTER.isEmpty()) {
                    data = new Data.Builder()
                            .putString("fileURI", selectedFilePath.toString())
                            .putBoolean("ADD_TO_EXISTING", addToExisting)//add to existing
                            .putString("USER_PHONE", user.getPhoneNumber())
                            .putStringArray("FILE_INFO", new String[]{
                                    selectClass.getSelectedItem() + "",
                                    selectSubject.getSelectedItem() + "",
                                    SELECTED_CHAPTER,
                                    fileTitle.getText() + "",
                                    UploadType,
                                    SELECTED_TEACHER_ID,
                                    privacy.isChecked() ? "private" : "public"
                            })
                            .build();
                    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadUtil.class)
                            .setInputData(data)
                            .build();

                    WorkManager.getInstance().enqueue(request);

                } else
                    Toast.makeText(context, "Check Title and Chapter of VIDEO", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(context, "SELECT FILE..", Toast.LENGTH_SHORT).show();
            uploadDialog.dismiss();
        });
        addExisting.setOnClickListener(v -> {
            addToExisting = true;
            AlertDialog.Builder alt = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.show_chapter_dialog, null);
            RecyclerView recyclerView = view.findViewById(R.id.show_chapter_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.hasFixedSize();
            recyclerView.setAdapter(fileAdapter);


            alt.setView(view);
            showChapterDialog = alt.create();

            showChapterDialog.show();
        });
        createNew.setOnClickListener(v -> {

            addToExisting = false;

            AlertDialog.Builder alert1 = new AlertDialog.Builder(context);

            alert1.setTitle("Add New Chapter");
            alert1.setMessage("Enter the Name of new Chapter");

            final EditText input = new EditText(context);
            alert1.setView(input);

            alert1.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (!input.getText().toString().isEmpty())
                        for (addExistingModel m : dataList)
                            if (m.Chapter.equalsIgnoreCase(input.getText().toString())) {
                                Toast.makeText(context, "Chapter Already Exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                    SELECTED_CHAPTER = input.getText().toString();


                }
            });

            alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert1.show();
        });
        uploadDialog = alert.create();
        uploadDialog.setCanceledOnTouchOutside(false);
        uploadDialog.show();
    }

    private void getChapters(String clas, String subject) {
        CollectionReference getChapters = FirebaseFirestore.getInstance()
                .collection("/CLASSES/" +
                        clas +
                        "/SUBJECT/" +
                        subject
                        + "/CONTENT"

                );
        getChapters
                .whereEqualTo("TEACHER_ID", user.getPhoneNumber())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dataList.clear();
                        fileAdapter = new showChapterAdpater(dataList);
                        if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                addExistingModel ad = new addExistingModel(doc.get("CHAPTER").toString(), doc.getId());
                                dataList.add(ad);
                                fileAdapter.notifyDataSetChanged();
                            }

                        } else
                            Toast.makeText(context, "NO CHAPTER FOUND", Toast.LENGTH_SHORT).show();
//                        progress.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                progress.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class showChapterAdpater extends RecyclerView.Adapter<uploadDialog.showChapterAdpater.ViewHolder> {
        ArrayList<addExistingModel> data;

        public showChapterAdpater(ArrayList<addExistingModel> dataList) {
            data = dataList;
        }


        @NonNull
        @Override
        public uploadDialog.showChapterAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_chapter_dialog_item, parent, false);
            return new uploadDialog.showChapterAdpater.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull uploadDialog.showChapterAdpater.ViewHolder holder, final int position) {
            holder.setTitle(data.get(position).Chapter);
            holder.layout.setOnClickListener(v -> {
                SELECTED_CHAPTER = data.get(position).Chapter;
                SELECTED_TEACHER_ID = data.get(position).DOCUMENT_PATH;
                showChapterDialog.dismiss();
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
