package org.wazir.build.elemenophee.ModelObj;

import java.util.ArrayList;

public class TeacherObj {
    String school, bio, experience;
    ArrayList<String> subs;
    ArrayList<Integer> classes;

    public TeacherObj() {
    }

    public TeacherObj(String school, String bio, String experience, ArrayList<String> subs, ArrayList<Integer> classes) {
        this.school = school;
        this.bio = bio;
        this.experience = experience;
        this.subs = subs;
        this.classes = classes;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public ArrayList<String> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<String> subs) {
        this.subs = subs;
    }

    public ArrayList<Integer> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Integer> classes) {
        this.classes = classes;
    }
}
