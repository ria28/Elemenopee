package org.wazir.build.elemenophee;

import org.wazir.build.elemenophee.Student.StuCommPanel.ComObject.SubComm;

import java.util.ArrayList;

public class ConstantsRet {
    public ConstantsRet() {
    }

    public static ArrayList<SubComm> getIconsForClass(String classTitle) {
        ArrayList<SubComm> list2 = new ArrayList<>();
        if (classTitle.equals("Class 5")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
        }
        if (classTitle.equals("Class 6")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if (classTitle.equals("Class 7")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if (classTitle.equals("Class 8")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sci, "Science"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_evs, "EVS"));
            list2.add(new SubComm(R.drawable.ic_gk, "G.K"));
        }
        if (classTitle.equals("Class 9")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_bio, "Biology"));
            list2.add(new SubComm(R.drawable.ic_physics, "Physics"));
            list2.add(new SubComm(R.drawable.ic_chem, "Chemistry"));
        }
        if (classTitle.equals("Class 10")) {
            list2.add(new SubComm(R.drawable.ic_maths, "Maths"));
            list2.add(new SubComm(R.drawable.ic_english, "English"));
            list2.add(new SubComm(R.drawable.ic_sst, "S.ST"));
            list2.add(new SubComm(R.drawable.ic_bio, "Biology"));
            list2.add(new SubComm(R.drawable.ic_physics, "Physics"));
            list2.add(new SubComm(R.drawable.ic_chem, "Chemistry"));
        }
        return list2;
    }
}
