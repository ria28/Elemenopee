package org.wazir.build.elemenophee;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProfilePicBottomModalSheet extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_pic_modal_sheet, container, false);
        TextView viewPic = view.findViewById(R.id.openProfilePic);
        TextView changePic = view.findViewById(R.id.ChangeProfilePic);
        TextView removePic =view.findViewById(R.id.RemoveProfilePic);

        viewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.BtnClicked("ViewPic");
                dismiss();
            }
        });
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.BtnClicked("ChangePic");
                dismiss();
            }
        });
        removePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.BtnClicked("RemovePic");
                dismiss();
            }
        });
        return view;
    }

    public interface BottomSheetListener{
        void BtnClicked(String BtnText);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
        mListener= (BottomSheetListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()
            + "must implement BottomSheetListener");
        }
    }
}
