package org.wazir.build.elemenophee.Student.Lecture.Subject.Object;

public class SubObj {private String subId;
    private String subTitle;
    private boolean clicked;

    public SubObj(String subId, String subTitle) {
        this.subId = subId;
        this.subTitle = subTitle;
        this.clicked = false;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public SubObj() {
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}

