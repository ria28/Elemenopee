package org.wazir.build.elemenophee.Student.Community;

public class Chapters {

    private String title;
    private String description;

    public Chapters(){

    }

    public Chapters(String title){
        this.title=title;
    }

    public Chapters(String title, String description){
        this.title=title;
        this.description=description;
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


}
