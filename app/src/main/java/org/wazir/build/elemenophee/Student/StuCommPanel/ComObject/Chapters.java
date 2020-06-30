package org.wazir.build.elemenophee.Student.StuCommPanel.ComObject;

public class Chapters {

    private String title;
    private String tID;
    private String SubName;
    private String Classs;
    private boolean isSubscriber;

    public Chapters(){

    }

    public Chapters(String title,String SubName){
        this.title=title;
        this.SubName=SubName;
    }

    public Chapters(String title, String tID, String subName, String classs, boolean isSubscriber) {
        this.title = title;
        this.tID = tID;
        SubName = subName;
        Classs = classs;
        this.isSubscriber = isSubscriber;
    }

    public boolean getIsSubscriber() {
        return isSubscriber;
    }

    public void setIsSubscriber(boolean isSubscriber) {
        this.isSubscriber = isSubscriber;
    }

    public String gettID() {
        return tID;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public String getClasss() {
        return Classs;
    }

    public void setClasss(String classs) {
        Classs = classs;
    }
}
