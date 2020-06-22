package org.wazir.build.elemenophee.Student.StudentSupport.Notification;

public class Data {

    private String user, body, title,sentTo;
    private int icon;

    public Data(String user, String body, String title, String sentTo, int icon) {
        this.user = user;
        this.body = body;
        this.title = title;
        this.sentTo = sentTo;
        this.icon = icon;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
