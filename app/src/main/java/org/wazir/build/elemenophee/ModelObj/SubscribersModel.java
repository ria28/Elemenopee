package org.wazir.build.elemenophee.ModelObj;

import java.sql.Timestamp;

public class SubscribersModel {
    String studentID;
    String expiry;


    public String getStudentID() {
        return studentID;
    }

    public SubscribersModel() {
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public SubscribersModel(String studentID, String expiry) {
        this.studentID = studentID;
        this.expiry = expiry;
    }
}
