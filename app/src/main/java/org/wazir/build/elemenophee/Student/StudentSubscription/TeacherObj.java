package org.wazir.build.elemenophee.Student.StudentSubscription;

import java.util.ArrayList;

public class TeacherObj {

    String TeacherName;
    String Description;
    ArrayList<String>subjects;

    public TeacherObj(String teacherName, String description, ArrayList<String> subjects) {
        TeacherName = teacherName;
        Description = description;
        this.subjects = subjects;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }
}
