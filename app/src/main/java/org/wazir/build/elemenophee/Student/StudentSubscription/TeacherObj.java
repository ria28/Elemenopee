package org.wazir.build.elemenophee.Student.StudentSubscription;

import java.util.ArrayList;

public class TeacherObj {

    String TeacherName;
    String Description;
    String picURL;
    ArrayList<String>subjects;
    String tID;

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String gettID() {
        return tID;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public TeacherObj(String teacherName, String description, String picURL, ArrayList<String> subjects, String tID) {
        TeacherName = teacherName;
        Description = description;
        this.picURL = picURL;
        this.subjects = subjects;
        this.tID = tID;
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
