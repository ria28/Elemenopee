package org.wazir.build.elemenophee.ModelObj;

import java.util.ArrayList;

public class TeacherObj {
    String school, bio, experience, name,phone;
    ArrayList<String> subs;
    ArrayList<Integer> classes;
    String proPicURL;
    int videoCount;

    public TeacherObj() {
    }


    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }


    public String getProPicURL() {
        return proPicURL;
    }

    public void setProPicURL(String proPicURL) {
        this.proPicURL = proPicURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
