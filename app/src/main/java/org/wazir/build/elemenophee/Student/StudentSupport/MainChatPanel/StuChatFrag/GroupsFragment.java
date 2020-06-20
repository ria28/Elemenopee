package org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel.StuChatFrag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.wazir.build.elemenophee.R;
import org.wazir.build.elemenophee.Student.StudentSupport.ChatGroup.GroupChatActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();
    Context context;

    private CollectionReference GroupRef;
    FirebaseAuth mAuth;
    String number;

    String groupID;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        mAuth=FirebaseAuth.getInstance();
        number = mAuth.getCurrentUser().getPhoneNumber();
        groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);


        IntializeFields();


        RetrieveAndDisplayGroups();



        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                String currentGroupName = adapterView.getItemAtPosition(position).toString();

                Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
                groupChatIntent.putExtra("groupName" , currentGroupName);
                startActivity(groupChatIntent);
            }
        });


        return groupFragmentView;
    }



    private void IntializeFields()
    {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);
    }

    private void RetrieveAndDisplayGroups()
    {

        GroupRef= FirebaseFirestore.getInstance().collection("ChatRoom")
                .document(groupID).collection("Groups");

        GroupRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        list_of_groups.add(doc.getData().toString());
                    }
                    arrayAdapter.notifyDataSetChanged();
                }else
                    Toast.makeText(getContext(),"No Groups Created Yet",Toast.LENGTH_SHORT).show();

            }
        });

    }
}