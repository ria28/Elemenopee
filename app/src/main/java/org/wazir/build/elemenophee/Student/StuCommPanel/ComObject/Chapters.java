package org.wazir.build.elemenophee.Student.StuCommPanel.ComObject;

public class Chapters {

    private String title;
    private String description;
    private String tID;
    private  String SubName;
    private String Classs;

    public Chapters(){

    }

    public Chapters(String title,String SubName){
        this.title=title;
        this.SubName=SubName;
    }

    public Chapters(String title, String description,String SubName,String Classs){
        this.title=title;
        this.description=description;
        this.SubName=SubName;
        this.Classs=Classs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
