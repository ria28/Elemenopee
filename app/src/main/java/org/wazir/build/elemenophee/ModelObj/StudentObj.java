package org.wazir.build.elemenophee.ModelObj;

import java.util.ArrayList;

public class StudentObj {
    String school, bio, target, contact;
    ArrayList<Integer> classes;

    public StudentObj() {
    }

    public StudentObj(String school, String bio, String target, String contact, ArrayList<Integer> classes) {
        this.school = school;
        this.bio = bio;
        this.target = target;
        this.contact = contact;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ArrayList<Integer> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Integer> classes) {
        this.classes = classes;
    }
}
