package org.wazir.build.elemenophee.IntLogSigScreens;

public class ChooseMoObj {
    private String text;
    private int clas;
    private boolean state;

    public ChooseMoObj(String text) {
        this.text = text;
        this.state = false;
        this.clas = -1;
    }

    public ChooseMoObj(int clas) {
        this.text = "";
        this.clas = clas;
        this.state = false;
    }

    public int getClas() {
        return clas;
    }

    public void setClas(int clas) {
        this.clas = clas;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}