package org.wazir.build.elemenophee.ModelObj;

import java.util.ArrayList;

public class StudentObj {
    String school, bio, target, contact, name;
    ArrayList<Integer> classes;
    ArrayList<String> contacts;
    private String mImageUrl;

    public StudentObj() {
    }


    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public StudentObj(String school, String bio, String target, String contact, String name, ArrayList<Integer> classes) {
        this.school = school;
        this.bio = bio;
        this.target = target;
        this.contact = contact;
        this.name = name;
        this.classes = classes;
    }



    public StudentObj(String mImageUrl){
        this.mImageUrl=mImageUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
