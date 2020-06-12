package org.wazir.build.elemenophee.Student.StuCommPanel.ComObject;

public class SubComm {

    private int image;
    String SubName;
    String str;

    public SubComm(){

    }
    public SubComm(String str){

        this.str=str;
    }

    public SubComm(int image,String SubName){
        this.image=image;
        this.SubName=SubName;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(int image) {
        this.SubName = SubName;
    }


}
