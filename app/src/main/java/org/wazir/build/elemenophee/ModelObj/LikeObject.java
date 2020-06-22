package org.wazir.build.elemenophee.ModelObj;

public class LikeObject {
    private String userId;

    public LikeObject() {
    }

    public LikeObject(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
