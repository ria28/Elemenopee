package org.wazir.build.elemenophee.IntLogSigScreens;

public class IntroMoObj {
    private int intro_img;
    private String intro_message, intro_title;

    IntroMoObj(int intro_img, String intro_title, String intro_message) {
        this.intro_img = intro_img;
        this.intro_message = intro_message;
        this.intro_title = intro_title;
    }

    int getIntro_img() {
        return intro_img;
    }

    public void setIntro_img(int intro_img) {
        this.intro_img = intro_img;
    }

    String getIntro_message() {
        return intro_message;
    }

    public void setIntro_message(String intro_message) {
        this.intro_message = intro_message;
    }

    String getIntro_title() {
        return intro_title;
    }

    public void setIntro_title(String intro_title) {
        this.intro_title = intro_title;
    }
}
