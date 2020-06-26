package org.wazir.build.elemenophee.ModelObj;

public class DownVideosModel {
    String title;
    String date;
    String uri;

    public DownVideosModel(String title, String date, String uri) {
        this.title = title;
        this.date = date;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
